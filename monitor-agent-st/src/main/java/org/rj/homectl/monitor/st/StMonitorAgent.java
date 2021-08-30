package org.rj.homectl.monitor.st;


import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigConstants;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.monitor.st.requests.DeviceFullStatusResponse;
import org.rj.homectl.monitor.st.requests.DeviceListingResponse;
import org.rj.homectl.service.ServiceBase;
import org.rj.homectl.spring.application.SpringApplicationContext;
import org.rj.homectl.st.model.DeviceStatus;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.producer.st.StStatusEventProducer;
import org.rj.homectl.status.st.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class StMonitorAgent extends ServiceBase {
    private static final Logger log = LoggerFactory.getLogger(StMonitorAgent.class);
    private AtomicBoolean active;
    private StStatusEventProducer producer;
    private RestTemplate client;
    private String sensorTarget;
    private String sensorToken;
    private StMonitorRequestTargets requestTargets;
    private List<String> devices;
    private int currentDevice;
    private long snapshotSendIntervalMs;
    private long lastFullSnapshot;
    private Map<String, StDeviceStatus> lastStatus;

    public static void main(String[] args) {
        SpringApplication.run(StMonitorAgent.class, args);
    }

    public StMonitorAgent(SpringApplicationContext context) {
        super(StMonitorAgent.class, context);
        this.active = new AtomicBoolean(false);
        this.lastFullSnapshot = 0L;
        this.currentDevice = 0;
        this.lastStatus = new HashMap<>();
    }

    @PostConstruct
    private void initialise() {
        this.sensorTarget = getSensorTarget();
        this.sensorToken = getSensorToken();
        this.requestTargets = getRequestTargets(sensorTarget);
        this.devices = getConfiguredDevices();
        this.snapshotSendIntervalMs = getSnapshotSendInterval();
        this.client = initialiseClient();
        this.producer = initialiseProducer();

        this.active.set(true);
        new Thread(this::execute).start();
    }

    private String getSensorTarget() {
        final var sensorTarget = String.format("%s%s",
                getConfig().get(ConfigEntry.MonitorSensor),
                getConfig().get(ConfigEntry.MonitorQuery));

        log.info("Monitor configured for sensor target \"{}\"", sensorTarget);
        return sensorTarget;
    }

    private String getSensorToken() {
        return getConfig().get(ConfigConstants.INTERNAL_TOKEN);
    }

    private StMonitorRequestTargets getRequestTargets(String sensorTarget) {
        final var targets = new StMonitorRequestTargets();
        targets.setListingRequest(String.format("%s%s", sensorTarget, getConfig().get(ConfigEntry.MonitorRequestListing)));
        targets.setStatusRequest(String.format("%s%s", sensorTarget, getConfig().get(ConfigEntry.MonitorRequestStatus)));

        return targets;
    }

    private List<String> getConfiguredDevices() {
        final var devices = IntStream.range(0, 100)
                .mapToObj(i -> getConfig().tryGet(String.format("%s[%d]", ConfigEntry.MonitorStDevices.getKey(), i)))
                .takeWhile(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (devices.isEmpty()) throw new RuntimeException("No configured devices");

        return devices;
    }

    private long getSnapshotSendInterval() {
        final var ms = getConfig().getLong(ConfigEntry.MonitorFullSnapshotSendIntervalMs);
        log.info("Monitor configured to send full snapshots every {}ms", ms);

        return ms;
    }

    private RestTemplate initialiseClient() {
        RestTemplate client = new RestTemplate();

        // TODO: Add retries and better error handling
        client.setErrorHandler(new DefaultResponseErrorHandler());

        return client;
    }

    private StStatusEventProducer initialiseProducer() {
        final var producerId = getConfig().get(ConfigEntry.ProducerId);
        final var producerConfigPath = getConfig().get(ConfigEntry.ProducerConfig);

        final var producer = new StStatusEventProducer(
                producerId,
                Config.load(producerConfigPath, getConfig()),
                KafkaProducer::new
        );

        return producer;
    }

    private void execute() {
        final var pollInterval = getConfig().getLong(ConfigEntry.MonitorPollIntervalMs);

        while (active.get()) {
            final var now = System.currentTimeMillis();

            if (shouldSendFullStatusSnapshot(lastFullSnapshot, now)) {
                sendFullStatusSnapshot(now);
                lastFullSnapshot = now;
            }
            else {
                sendDeviceEvents(currentDevice, lastStatus, now);
                currentDevice = (currentDevice + 1) % devices.size();
            }

            Util.threadSleepOrElse(pollInterval,
                    ex -> log.error("Failed to suspend monitor thread ({})", ex.getMessage()));
        }
    }

    private boolean shouldSendFullStatusSnapshot(long lastSnapshotTimestamp, long currentTimestamp) {
        return (currentTimestamp - lastSnapshotTimestamp) >= snapshotSendIntervalMs;
    }

    private void sendFullStatusSnapshot(long now) {
        final var listing = getData(requestTargets.getListingRequest(), sensorToken, now, DeviceListingResponse.class);
        final var status = new ArrayList<>(lastStatus.values());

        final var statusSnapshot = new StStatusData(StEventType.Snapshot,
                new StDeviceListing(listing.getItems()),
                new StDeviceStatuses(status));

        log.info("Sending full status snapshot at {}", now);
        produceData(statusSnapshot);
    }

    private void sendDeviceEvents(int deviceIx, Map<String, StDeviceStatus> lastStatus, long now) {
        final var device = devices.get(deviceIx);
        final var target = requestTargets.getStatusRequest().replace(ConfigConstants.DEVICE_PLACEHOLDER, device);
        final var status = getData(target, sensorToken, now, DeviceFullStatusResponse.class);
        final var deviceStatus = new StDeviceStatus(device, status);

        final var delta = calculateDelta(lastStatus.get(device), deviceStatus);
        if (delta.isEmpty()) {
            log.debug("No status events to report for device {} at {}", device, now);
        }
        else {
            log.info("Publishing detected status event for device {} at {}", device, now);
            final var eventData = new StStatusData(StEventType.Events, null, new StDeviceStatuses(delta));

            produceData(eventData);
            lastStatus.put(device, delta.get(0));
        }
    }

    private List<StDeviceStatus> calculateDelta(StDeviceStatus lastStatus, StDeviceStatus status) {
        if (lastStatus == null) return List.of(status);
        if (!Objects.equals(lastStatus, status)) return List.of(status);

        return List.of();
    }

    private void produceData(StStatusData status) {
        producer.send(StatusEventType.ST.getKey(), status);
    }


    private <T> T getData(String target, String token, long now, Class<T> responseClass) {
        final HttpEntity<String> entity = getRequestEntity(token);
        final var data = client.exchange(target, HttpMethod.GET, entity, responseClass);

        log.debug("Received \"{} ({})\" response from ST service [{}] at {}",
                data.getStatusCode(), data.getStatusCodeValue(), target, now);

        return data.getBody();
    }

    private HttpEntity<String> getRequestEntity(String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        return new HttpEntity<>("body", headers);
    }

    @Override
    protected boolean handleTerminationRequest(String reason) {
        return true;    // Allow all requests
    }
}