package org.rj.homectl.aggregation.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class EventConsumer {

    private volatile boolean active = true;
    private final Properties properties;
    private final Consumer<String, String> consumer;
    private final ConsumerRecordsHandler<String, String> recordsHandler;

    public EventConsumer(final Properties properties,
                         final Consumer<String, String> consumer,
                         final ConsumerRecordsHandler<String, String> recordsHandler) {
        this.properties = properties;
        this.consumer = consumer;
        this.recordsHandler = recordsHandler;
    }

    public void execute() {
        try {
            consumer.subscribe(List.of(properties.getProperty("input.topic.name")));

            while (active) {
                final var records = consumer.poll(Duration.ofSeconds(1));
                recordsHandler.process(records);
            }
        }
        finally {
            consumer.close();
        }
    }

    public void shutdown() {
        this.active = false;
    }

}
