package org.rj.homectl.aggregation;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.rj.homectl.aggregation.consumer.EventConsumer;
import org.rj.homectl.aggregation.consumer.TmpDataHandler;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.consumer.status.StatusEventConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.rj")
public class Aggregation {

    public static void main(String[] args) {
        SpringApplication.run(Aggregation.class, args);
    }

    public Aggregation() {
        final var config = Config.load("config/event-consumer.properties");

        final var statusConsumer = new StatusEventConsumer(
                "status-consumer-01",
                config,
                new KafkaConsumer<>(config.getProperties()),
                new TmpDataHandler());

        Runtime.getRuntime().addShutdownHook(new Thread(statusConsumer::shutdown));

        new Thread(statusConsumer::execute).start();
    }

}