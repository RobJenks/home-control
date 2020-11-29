package org.rj.homectl.aggregation;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.rj.homectl.aggregation.consumer.EventConsumer;
import org.rj.homectl.aggregation.consumer.TmpDataHandler;
import org.rj.homectl.common.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Aggregation {

    public static void main(String[] args) {
        SpringApplication.run(Aggregation.class, args);
    }

    public Aggregation() {
        final var config = Config.load("config/event-consumer.properties");

        final var eventConsumer = new EventConsumer(
                config.getProperties(),
                new KafkaConsumer<>(config.getProperties()),
                new TmpDataHandler());

        Runtime.getRuntime().addShutdownHook(new Thread(eventConsumer::shutdown));

        new Thread(eventConsumer::execute).start();
    }

}