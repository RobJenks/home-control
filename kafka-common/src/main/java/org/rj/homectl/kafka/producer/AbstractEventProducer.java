package org.rj.homectl.kafka.producer;

import org.apache.kafka.clients.producer.Producer;
import org.rj.homectl.common.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public abstract class AbstractEventProducer<K, V> implements EventProducer<K, V> {
    private final Logger log;
    private final String id;
    private final Config config;
    private final Producer<K, V> producer;

    public AbstractEventProducer(final String id,
                                 final Config config,
                                 final ProducerGenerator<K, V> producerGenerator,
                                 final Class<?> implementationClass) {
        this.log = LoggerFactory.getLogger(implementationClass);
        log.info("Initialising Producer \"{}\" of type \"{}\"", id, implementationClass.getSimpleName());

        this.id = id;
        this.config = config;
        this.producer = producerGenerator.apply(producerConfig());
    }

    @Bean
    public Map<String, Object> producerConfig() {
        final var producerConfig = config.toMap();

        // Add base properties not specified in external config

        return producerConfig;
    }

    public String getId() { return id; }
    protected Config getConfig() { return config; }
    protected Producer<K, V> getProducer() { return this.producer; }

    protected Logger log() { return this.log; }

    protected abstract Class<?> getKeyClass();
    protected abstract Class<?> getValueClass();

}
