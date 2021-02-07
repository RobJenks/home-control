package org.rj.homectl.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.Deserializer;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.config.ConfigEntry;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.consumer.events.AbstractConsumerEvent;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;
import org.rj.homectl.kafka.consumer.handlers.LoggingRecordHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/*
   Custom factory initialisation: https://github.com/SpringOnePlatform2016/grussell-spring-kafka/blob/master/s1p-kafka/src/main/java/org/s1p/JsonConfiguration.java#L66
*/

public abstract class AbstractEventConsumer<K, V extends AbstractConsumerEvent> implements EventConsumer<K, V> {
    private volatile boolean active = true;
    private final Logger log;
    private final String id;
    private final Config config;
    private final Consumer<K, V> consumer;
    private final ConsumerRecordsHandler<K, V> recordsHandler;
    private final Duration pollDuration;

    public AbstractEventConsumer(final String id,
                                 final Config config,
                                 final ConsumerGenerator<K, V> consumerGenerator,
                                 final ConsumerRecordsHandler<K, V> recordsHandler,
                                 final Class<?> implementationClass) {
        this.log = LoggerFactory.getLogger(implementationClass);
        log.info("Initialising consumer \"{}\" of type \"{}\"", id, implementationClass.getSimpleName());

        this.id = id;
        this.config = config;
        this.consumer = consumerGenerator.apply(consumerConfig());
        this.recordsHandler = recordsHandler;
        this.pollDuration = getPollDuration(config);
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        final var consumerConfig = config.toMap();

        // Add base properties not specified in external config
        consumerConfig.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, getKeyDeserializerClass());
        consumerConfig.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, getValueDeserializerClass());

        return consumerConfig;
    }

    @Bean
    public ConsumerFactory<K, V> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(),
                buildErrorHandlingKeyDeserializer(provideKeyDeserializer()),
                buildErrorHandlingValueDeserializer(provideValueDeserializer()));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<K, V> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<K, V> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());

        return containerFactory;
    }

    private Deserializer<K> buildErrorHandlingKeyDeserializer(Optional<Supplier<Deserializer<K>>> provider) {
        return buildErrorHandlingDeserializer(provider, true, this::handleKeyDeserializationFailure);
    }

    private Deserializer<V> buildErrorHandlingValueDeserializer(Optional<Supplier<Deserializer<V>>> provider) {
        return buildErrorHandlingDeserializer(provider, false, this::handleValueDeserializationFailure);
    }

    private <T> Deserializer<T> buildErrorHandlingDeserializer(Optional<Supplier<Deserializer<T>>> provider, boolean forKey,
                                                               Function<FailedDeserializationInfo, T> failedDeserializationFunction) {
        final var deser = provider.orElse(() -> null).get();
        final var handler = new ErrorHandlingDeserializer<>(deser);

        handler.setForKey(forKey);
        handler.setFailedDeserializationFunction(failedDeserializationFunction);

        return handler;
    }

    protected abstract K handleKeyDeserializationFailure(FailedDeserializationInfo failureInfo);
    protected abstract V handleValueDeserializationFailure(FailedDeserializationInfo failureInfo);

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

    protected static <K, V extends AbstractConsumerEvent> ConsumerRecordsHandler<K, V> defaultRecordHandler() {
        return new LoggingRecordHandler<>();
    }

    protected Logger log() {
        return this.log;
    }

    protected abstract Class<?> getKeyClass();
    protected abstract Class<?> getValueClass();

    @SuppressWarnings("rawtypes")
    protected abstract Class<? extends Deserializer> getKeyDeserializerClass();
    @SuppressWarnings("rawtypes")
    protected abstract Class<? extends Deserializer> getValueDeserializerClass();

    protected abstract Optional<Supplier<Deserializer<K>>> provideKeyDeserializer();
    protected abstract Optional<Supplier<Deserializer<V>>> provideValueDeserializer();

    @Override
    public void shutdown() {
        log.info("Shutting down consumer \"{}\"", id);
        this.active = false;
    }
}
