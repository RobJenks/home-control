package org.rj.homectl.monitor.hue;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigConstants;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.service.ServiceBase;
import org.rj.homectl.spring.application.SpringApplicationContext;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.hue.HueStatusData;
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
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class HueMonitorAgent extends ServiceBase {
    private static final Logger log = LoggerFactory.getLogger(HueMonitorAgent.class);
    private AtomicBoolean active;
    private HueStatusEventProducer producer;
    private RestTemplate client;
    private String sensorTarget;
    private String sensorToken;

    public static void main(String[] args) {
        SpringApplication.run(HueMonitorAgent.class, args);
    }

    public HueMonitorAgent(SpringApplicationContext context) {
        super(HueMonitorAgent.class, context);
        this.active = new AtomicBoolean(false);
    }

    @PostConstruct
    private void initialise() {
        this.sensorTarget = getSensorTarget();
        this.sensorToken = getSensorToken();
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
        final var tokenPath = getConfig().get(ConfigEntry.MonitorToken);
        String token;
        try {
            token = Util.loadStringResource(tokenPath);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to load configured token data");
        }

        if (StringUtils.isBlank(token)) throw new RuntimeException("No valid token available");
        return token;
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
            log.debug("Requesting status from Hue service [{}]", sensorTarget);

            final var status = getData(sensorTarget, sensorToken);
            // TODO: Analyse data and report events/diffs
            producer.send(StatusEventType.Hue.getKey(), status);

            Util.threadSleepOrElse(pollInterval,
                    ex -> log.error("Failed to suspend monitor thread ({})", ex.getMessage()));
        }
    }

    private HueStatusData getData(String sensorTarget, String token) {
        final var target = sensorTarget.replace(ConfigConstants.TOKEN_PLACEHOLDER, token);
        final var data = client.getForEntity(target, HueStatusData.class);

        log.debug("Received \"{} ({})\" response from Hue service: {}",
                data.getStatusCode(), data.getStatusCodeValue(), Util.safeSerialize(data.getBody()));

        return data.getBody();
    }

    @Override
    protected boolean handleTerminationRequest(String reason) {
        return true;    // allow all requests
    }
}
