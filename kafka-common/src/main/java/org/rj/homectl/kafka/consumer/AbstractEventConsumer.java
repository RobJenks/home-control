package org.rj.homectl.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class AbstractEventConsumer<K, V> implements EventConsumer<K, V> {
    private volatile boolean active = true;
    private final Logger log;
    private final String id;
    private final Config config;
    private final Consumer<K, V> consumer;
    private final ConsumerRecordsHandler<K, V> recordsHandler;
    private final Duration pollDuration;

    public AbstractEventConsumer(final String id,
                                 final Config config,
                                 final Consumer<K, V> consumer,
                                 final ConsumerRecordsHandler<K, V> recordsHandler,
                                 final Class<?> implementationClass) {
        this.log = LoggerFactory.getLogger(implementationClass);
        log.info("Intialising consumer \"{}\" of type \"{}\"", id, implementationClass.getSimpleName());

        this.id = id;
        this.config = config;
        this.consumer = consumer;
        this.recordsHandler = recordsHandler;
        this.pollDuration = getPollDuration(config);
    }

    public void execute() {
        try {
            final var topics = config.get(ConfigEntry.InputTopicNames);
            consumer.subscribe(Util.listFromString(topics, ","));

            while (active) {
                final var records = consumer.poll(pollDuration);
                process(records);
            }
        }
        finally {
            consumer.close();
        }
    }

    @Override
    public void process(ConsumerRecords<K, V> records) {
        log.debug("Consumer \"{}\" processing {} records", id, records.count());
        recordsHandler.process(records);
    }

    private static Duration getPollDuration(Config config) {
        final var secs = config.getLong(ConfigEntry.PollDurationSec);
        if (secs < 1L) throw new RuntimeException("Cannot set consumer poll duration <1 sec, requested duration was " + secs);

        return Duration.ofSeconds(secs);
    }

    @Override
    public void shutdown() {
        log.info("Shutting down consumer \"{}\"", id);
        this.active = false;
    }
}
