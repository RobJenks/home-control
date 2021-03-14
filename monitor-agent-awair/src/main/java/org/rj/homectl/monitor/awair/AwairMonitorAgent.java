package org.rj.homectl.monitor.awair;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.producer.AbstractEventProducer;
import org.rj.homectl.kafka.producer.ProducerGenerator;
import org.rj.homectl.status.awair.AwairStatusData;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.producer.awair.AwairStatusEventProducer;
import org.rj.homectl.status.serde.StatusEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@ComponentScan(basePackages = "com.rj")
public class AwairMonitorAgent {
    private static final Logger log = LoggerFactory.getLogger(AwairMonitorAgent.class);

    public static void main(String[] args) {
        SpringApplication.run(AwairMonitorAgent.class, args);
    }

    public AwairMonitorAgent() {
        final var config = Config.load("config/status-monitor-awair.properties");

        final var producer = new AwairStatusEventProducer(
                "awair-agent-producer-01",
                config,
                KafkaProducer::new,
                AwairMonitorAgent.class
        );

        while (true) {
            final var status = getTempStatus();

            log.info("Sending status ({})", Util.safeSerialize(status));
            producer.send(StatusEventType.Awair.getKey(), status);
            try {
                Thread.sleep(1000L);
            }
            catch (Exception ex) {
                log.error("*** Failed to suspend thread ({}) ***", ex.getMessage());
            }
        }
    }

    private AwairStatusData getTempStatus() {
        final var rand = new Random(System.currentTimeMillis());
        final var status = new AwairStatusData();
        status.setTimestamp(OffsetDateTime.now());
        status.setAbsoluteHumidity(rand.nextFloat() * 100.0f);
        status.setCO2(rand.nextInt(500));
        status.setTemp(rand.nextFloat() * 10.0f + 18.0f);
        status.setDewPoint(rand.nextFloat() * 100.0f);
        status.setEstimatedCO2(rand.nextInt(500));
        status.setEstimatedPM10(rand.nextInt(1000));
        status.setHumidity(rand.nextFloat() * 100.0f);
        status.setPM25(rand.nextInt(1000));
        status.setVOC(rand.nextInt(1000));
        status.setVOCBaseline((long)rand.nextInt(1000));
        status.setVOCRawEthanol(rand.nextInt(1000));
        status.setVOCRawH2(rand.nextInt(1000));
        status.setScore(rand.nextInt(20) + 80);

        return status;
    }
}
