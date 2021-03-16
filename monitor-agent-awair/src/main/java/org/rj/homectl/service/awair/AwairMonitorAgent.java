package org.rj.homectl.service.awair;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.util.Util;
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
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class AwairMonitorAgent extends ServiceBase {
    private static final Logger log = LoggerFactory.getLogger(AwairMonitorAgent.class);
    private AtomicBoolean active;

    public static void main(String[] args) {
        SpringApplication.run(AwairMonitorAgent.class, args);
    }

    public AwairMonitorAgent(SpringApplicationContext context) {
        super(AwairMonitorAgent.class, context);
        this.active = new AtomicBoolean(false);
    }

    @PostConstruct
    private void initialise() {
        final var producer = new AwairStatusEventProducer(
                "awair-agent-producer-01",
                Config.load("config/status-monitor-awair-producer.properties"),
                KafkaProducer::new,
                AwairMonitorAgent.class
        );

        this.active.set(true);
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

    @Override
    protected boolean handleTerminationRequest(String reason) {
        return true;    // Allow all requests
    }
}
