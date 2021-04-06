package org.rj.homectl.monitor.hue;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.jooq.lambda.tuple.*;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigConstants;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.hue.model.Light;
import org.rj.homectl.hue.model.State;
import org.rj.homectl.service.ServiceBase;
import org.rj.homectl.spring.application.SpringApplicationContext;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.hue.*;
import org.rj.homectl.status.producer.hue.HueStatusEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.jooq.lambda.tuple.Tuple.*;


@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class HueMonitorAgent extends ServiceBase {
    private static final Logger log = LoggerFactory.getLogger(HueMonitorAgent.class);
    private AtomicBoolean active;
    private HueStatusEventProducer producer;
    private RestTemplate client;
    private String sensorTarget;
    private String sensorToken;
    private long snapshotSendIntervalMs;
    private long lastFullSnapshot;
    private HueStatusLightData lastStatus;

    public static void main(String[] args) {
        SpringApplication.run(HueMonitorAgent.class, args);
    }

    public HueMonitorAgent(SpringApplicationContext context) {
        super(HueMonitorAgent.class, context);
        this.active = new AtomicBoolean(false);
        this.lastFullSnapshot = 0L;
        this.lastStatus = null;
    }

    @PostConstruct
    private void initialise() {
        this.sensorTarget = getSensorTarget();
        this.sensorToken = getSensorToken();
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

    private HueStatusEventProducer initialiseProducer() {
        final var producerId = getConfig().get(ConfigEntry.ProducerId);
        final var producerConfigPath = getConfig().get(ConfigEntry.ProducerConfig);

        final var producer = new HueStatusEventProducer(
                producerId,
                Config.load(producerConfigPath),
                KafkaProducer::new
        );

        return producer;
    }

    private void execute() {
        final var pollInterval = getConfig().getLong(ConfigEntry.MonitorPollIntervalMs);

        while (active.get()) {
            final var now = System.currentTimeMillis();

            log.trace("Requesting status from Hue service [{}] at {}", sensorTarget, now);
            final var status = getData(sensorTarget, sensorToken, now);

            if (shouldSendFullStatusSnapshot(lastFullSnapshot, now) || lastStatus == null) {
                sendFullStatusSnapshot(status, now);
                lastFullSnapshot = now;
            }

            sendEvents(lastStatus, status, now);
            lastStatus = status;

            Util.threadSleepOrElse(pollInterval,
                    ex -> log.error("Failed to suspend monitor thread ({})", ex.getMessage()));
        }
    }

    private boolean shouldSendFullStatusSnapshot(long lastSnapshotTimestamp, long currentTimestamp) {
        return (currentTimestamp - lastSnapshotTimestamp) >= snapshotSendIntervalMs;
    }

    private void sendFullStatusSnapshot(HueStatusLightData status, long now) {
        log.info("Sending full status snapshot at {}", now);
        produceData(HueStatusData.snapshot(status));
    }

    private void sendEvents(HueStatusLightData lastStatus, HueStatusLightData status, long now) {
        final var delta = calculateDelta(lastStatus, status);
        if (delta.isEmpty()) {
            log.debug("No status events to report at {}", now);
            return;
        }

        log.info("Publishing {} detected status updates and {} events at {}", delta.getUpdates().size(), delta.getEvents().size(), now);

        produceData(HueStatusData.updates(delta.getUpdates()));
        delta.getEvents().forEach(event -> produceData(HueStatusData.event(event)));
    }

    private HueDeltaDetails calculateDelta(HueStatusLightData lastStatus, HueStatusLightData status) {
        HueDeltaDetails delta = new HueDeltaDetails();

        final var oldStatus = Optional.ofNullable(lastStatus).orElseGet(HueStatusLightData::new);
        final var newStatus = Optional.ofNullable(status).orElseGet(HueStatusLightData::new);

        // Updated and removed devices
        oldStatus.entrySet().stream()
                .map(last -> tuple(last, newStatus.get(last.getKey())))
                .filter(x -> !Objects.equals(x.v1.getValue(), x.v2))
                .map(x -> calculateDeltaForDevice(x.v1.getKey(), x.v1.getValue(), x.v2))
                .forEach(delta::addFrom);

        // New devices
        newStatus.entrySet().stream()
                .filter(x -> !oldStatus.containsKey(x.getKey()))
                .map(data -> calculateDeltaForDevice(data.getKey(), null, data.getValue()))
                .forEach(delta::addFrom);

        return delta;
    }

    // Method is called on objects which have already been established to be different
    private HueDeltaDetails calculateDeltaForDevice(String id, Light lastStatus, Light status) {
        if (lastStatus == null && status == null) return new HueDeltaDetails();
        if (lastStatus == null) return newDeviceEvents(id, status);
        if (status == null) return removedDeviceEvents(id, lastStatus);

        final var deltas = new HueDeltaDetails();
        deltas.addUpdate(id, status);

        final var diff = Util.mapDifference(lastStatus, status);
        final boolean wasOn = Optional.ofNullable(lastStatus.getState()).map(State::getOn).orElse(false);
        final boolean isOn = Optional.ofNullable(status.getState()).map(State::getOn).orElse(false);

        if (isOn != wasOn) {
            deltas.addEvent(new HueStatusEventDetails(id, isOn ? HueDeviceEventType.TurnedOn : HueDeviceEventType.TurnedOff, diff));
        }

        deltas.addEvent(new HueStatusEventDetails(id, HueDeviceEventType.StateChange, diff));
        return deltas;
    }

    private HueDeltaDetails newDeviceEvents(String id, Light status) {
        return new HueDeltaDetails(
                new HueStatusLightData(Map.of(id, status)),
                List.of(new HueStatusEventDetails(id, HueDeviceEventType.DeviceAdded,
                        Maps.difference(Map.of(), Util.convertToJsonMap(status))))
        );
    }

    private HueDeltaDetails removedDeviceEvents(String id, Light lastStatus) {
        return new HueDeltaDetails(
                new HueStatusLightData(Map.of()),
                List.of(new HueStatusEventDetails(id, HueDeviceEventType.DeviceRemoved,
                        Maps.difference(Util.convertToJsonMap(lastStatus), Map.of())))
        );
    }

    private void produceData(HueStatusData status) {
        producer.send(StatusEventType.Hue.getKey(), status);
    }


    private HueStatusLightData getData(String sensorTarget, String token, long now) {
        final var target = sensorTarget.replace(ConfigConstants.TOKEN_PLACEHOLDER, token);
        final var data = client.getForEntity(target, HueStatusLightData.class);

        log.debug("Received \"{} ({})\" response from Hue service [{}] at {}",
                data.getStatusCode(), data.getStatusCodeValue(), sensorTarget, now);

        return data.getBody();
    }

    @Override
    protected boolean handleTerminationRequest(String reason) {
        return true;    // allow all requests
    }
}
