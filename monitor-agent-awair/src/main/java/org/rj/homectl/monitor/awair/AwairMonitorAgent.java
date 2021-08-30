package org.rj.homectl.monitor.awair;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.monitor.awair.config.SensorTarget;
import org.rj.homectl.service.ServiceBase;
import org.rj.homectl.spring.application.SpringApplicationContext;
import org.rj.homectl.status.awair.AwairStatusData;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.producer.awair.AwairStatusEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class AwairMonitorAgent extends ServiceBase {
    private static final Logger log = LoggerFactory.getLogger(AwairMonitorAgent.class);
    private AtomicBoolean active;
    private AwairStatusEventProducer producer;
    private RestTemplate client;
    private List<SensorTarget> sensorTargets;

    public static void main(String[] args) {
        SpringApplication.run(AwairMonitorAgent.class, args);
    }

    public AwairMonitorAgent(SpringApplicationContext context) {
        super(AwairMonitorAgent.class, context);
        this.active = new AtomicBoolean(false);
    }

    @PostConstruct
    private void initialise() {
        this.sensorTargets = getSensorTargets();
        this.client = initialiseClient();
        this.producer = initialiseProducer();


        this.active.set(true);
        new Thread(this::execute).start();
    }

    private List<SensorTarget> getSensorTargets() {
        final var query = getConfig().tryGet(ConfigEntry.MonitorQuery)
                .orElseThrow(() -> new RuntimeException("No configured monitor query"));

        final var sensors = IntStream.range(0, 100)
                .mapToObj(i -> getConfig().tryGet(String.format("%s[%d]", ConfigEntry.MonitorSensors.getKey(), i)))
                .takeWhile(Optional::isPresent)
                .map(Optional::get)
                .map(service -> new SensorTarget(service, String.format("%s%s", service, query)))
                .collect(Collectors.toList());

        if (sensors.isEmpty()) throw new RuntimeException("No sensors configured for status monitor");

        log.info("Monitor configured for {} sensor targets: {}", sensors.size(), Util.safeSerialize(sensors));
        return sensors;
    }

    private RestTemplate initialiseClient() {
        RestTemplate client = new RestTemplate();

        // TODO: Add retries and better error handling
        client.setErrorHandler(new DefaultResponseErrorHandler());

        return client;
    }

    private AwairStatusEventProducer initialiseProducer() {
        final var producerId = getConfig().tryGet(ConfigEntry.ProducerId)
                .orElseThrow(() -> new RuntimeException("Initialisation failed; no configured producer ID"));

        final var producerConfigPath = getConfig().tryGet(ConfigEntry.ProducerConfig)
                .orElseThrow(() -> new RuntimeException("Initialisation failed; no producer config provided"));

        final var producer = new AwairStatusEventProducer(
                producerId,
                Config.load(producerConfigPath, getConfig()),
                KafkaProducer::new
        );

        return producer;
    }

    private void execute() {
        int currentSensor = 0;
        final var pollInterval = getConfig().getLong(ConfigEntry.MonitorPollIntervalMs);

        while (active.get()) {
            final var sensor = sensorTargets.get(currentSensor);
            log.debug("Requesting status from Awair service {} [{}]", currentSensor, sensor);

            final var status = getData(sensor);
            producer.send(StatusEventType.Awair.getKey(), status);

            Util.threadSleepOrElse(pollInterval,
                    ex -> log.error("Failed to suspend monitor thread ({})", ex.getMessage()));

            currentSensor = (currentSensor + 1) % sensorTargets.size();
        }
    }

    private AwairStatusData getData(SensorTarget sensor) {
        final var data = client.getForEntity(sensor.getTarget(), AwairStatusData.class);

        log.debug("Received \"{} ({})\" response from Awair service: {}",
                data.getStatusCode(), data.getStatusCodeValue(), Util.safeSerialize(data.getBody()));

        final var status = Optional.ofNullable(data.getBody()).orElseGet(AwairStatusData::new);
        status.setDeviceId(sensor.getId());

        return status;
    }

    @Override
    protected boolean handleTerminationRequest(String reason) {
        return true;    // allow all requests
    }
}
