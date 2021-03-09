package org.rj.homectl.status.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.rj.homectl.kafka.consumer.error.ConsumerEventError;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.kafka.consumer.AbstractEventConsumer;
import org.rj.homectl.kafka.consumer.ConsumerGenerator;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;
import org.rj.homectl.status.events.StatusEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class StatusEventConsumer<T> extends AbstractEventConsumer<String, StatusEvent<T>> {
    private final Supplier<StatusEvent<T>> statusEventFactory;

    @Autowired
    public StatusEventConsumer(final String id,
                               final Config config,
                               final Supplier<StatusEvent<T>> statusEventFactory,
                               final Optional<ConsumerGenerator<String, StatusEvent<T>>> consumerGenerator,
                               final Optional<ConsumerRecordsHandler<String, StatusEvent<T>>> recordsHandler) {
        super(id, config,
                consumerGenerator.orElse(StatusEventConsumer::defaultConsumerGenerator),
                recordsHandler.orElseGet(AbstractEventConsumer::defaultRecordHandler),
                StatusEventConsumer.class);

        this.statusEventFactory = statusEventFactory;
    }

    @Override
    protected Class<?> getKeyClass() {
        return String.class;
    }

    @Override
    protected Class<?> getValueClass() {
        return StatusEvent.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Class<? extends Deserializer> getKeyDeserializerClass() {
        return StringDeserializer.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Class<? extends Deserializer> getValueDeserializerClass() {
        return JsonDeserializer.class;
    }

    @Override
    protected Optional<Supplier<Deserializer<String>>> provideKeyDeserializer() {
        return Optional.empty();
    }

    @Override
    protected Optional<Supplier<Deserializer<StatusEvent<T>>>> provideValueDeserializer() {
        return Optional.of(() -> new JsonDeserializer<StatusEvent<T>>()
                .trustedPackages("*"));
    }

    @Override
    protected String handleKeyDeserializationFailure(FailedDeserializationInfo failureInfo) {
        log().error("Failed to deserialize status event key [{}]", failureInfo);
        return StatusEventType.Unknown.getKey();
    }

    @Override
    protected StatusEvent<T> handleValueDeserializationFailure(FailedDeserializationInfo failureInfo) {
        log().error("Failed to deserialize status event value [{}]", failureInfo);

        StatusEvent<T> errorEvent = statusEventFactory.get();
        errorEvent.setError(ConsumerEventError.fromFailedDeserializationInfo(failureInfo));
        return errorEvent;
    }

    private static <T> Consumer<String, StatusEvent<T>> defaultConsumerGenerator(Map<String, Object> consumerConfig) {
        return new KafkaConsumer<>(consumerConfig);
    }
}

