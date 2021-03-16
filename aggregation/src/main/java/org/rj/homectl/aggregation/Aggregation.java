package org.rj.homectl.aggregation;

import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.service.ServiceBase;
import org.rj.homectl.spring.application.SpringApplicationContext;
import org.rj.homectl.status.consumer.StatusEventConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Optional;


@SpringBootApplication
@ComponentScan(basePackages = "org.rj")
public class Aggregation extends ServiceBase {

    public static void main(String[] args) {
        SpringApplication.run(Aggregation.class, args);
    }

    public Aggregation(SpringApplicationContext context) {
        super(Aggregation.class, context);

    }

    @PostConstruct
    private void initialise() {
        final var consumerId = getConfig().get(ConfigEntry.ConsumerId);
        final var consumerConfigPath = getConfig().get(ConfigEntry.ConsumerConfig);

        final var statusConsumer = new StatusEventConsumer(
                consumerId,
                Config.load(consumerConfigPath),
                Optional.empty(),
                Optional.empty());

        new Thread(statusConsumer::execute).start();
    }

    @Override
    protected boolean handleTerminationRequest(String reason) {
        return true;    // accept all requests
    }
}