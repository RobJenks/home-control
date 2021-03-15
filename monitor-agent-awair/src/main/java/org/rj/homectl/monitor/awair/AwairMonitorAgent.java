package org.rj.homectl.monitor.awair;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.status.awair.AwairStatusData;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.producer.awair.AwairStatusEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class AwairMonitorAgent {
    private static final Logger log = LoggerFactory.getLogger(AwairMonitorAgent.class);
    private final AwairMonitorAgentService service;
    private AtomicBoolean active;

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(AwairMonitorAgent.class, args);
    }

    public AwairMonitorAgent() {
        this.active = new AtomicBoolean(true);
        this.service = new AwairMonitorAgentService(this);

        final var config = Config.load("config/status-monitor-awair.properties");

        final var producer = new AwairStatusEventProducer(
                "awair-agent-producer-01",
                config,
                KafkaProducer::new,
                AwairMonitorAgent.class
        );

        Runtime.getRuntime().addShutdownHook(new Thread(this::preShutdown));

        new Thread(() -> execute(producer)).start();
    }

    private void execute(AwairStatusEventProducer producer) {
        while (this.active.get()) {
            log.debug("Requesting status from Awair service");
            final var status = getData();

            log.debug("Sending status ({})", Util.safeSerialize(status));
            producer.send(StatusEventType.Awair.getKey(), status);
            try {
                Thread.sleep(1000L);
            }
            catch (Exception ex) {
                log.error("*** Failed to suspend monitor thread ({}) ***", ex.getMessage());
            }
        }
    }

    private AwairStatusData getData() {
        RestTemplate restTemplate = new RestTemplate();
        final var data = restTemplate
                .getForEntity("http://awair-elem-141ea1.local/air-data/latest", AwairStatusData.class);

        log.info("Received \"{} ({})\" response from Awair service: {}",
                data.getStatusCode(), data.getStatusCodeValue(), Util.safeSerialize(data.getBody()));

        return data.getBody();
    }

    public void terminate(String reason) {
        log.info("Received termination signal ({})", reason);
        this.active.set(false);
        SpringApplication.exit(applicationContext, () -> 0);
    }

    private void preShutdown() {
        log.info("JVM shutdown initiated, ending process");
    }
}
