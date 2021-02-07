package org.rj.homectl.status.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.rj.homectl.kafka.consumer.error.ConsumerEventError;
import org.rj.homectl.status.errors.StatusEventError;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.status.awair.AwairStatusEvent;
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
public class StatusEventConsumer extends AbstractEventConsumer<String, StatusEvent> {

    @Autowired
    public StatusEventConsumer(final String id,
                               final Config config,
                               final Optional<ConsumerGenerator<String, StatusEvent>> consumerGenerator,
                               final Optional<ConsumerRecordsHandler<String, StatusEvent>> recordsHandler) {
        super(id, config,
                consumerGenerator.orElse(StatusEventConsumer::defaultConsumerGenerator),
                recordsHandler.orElseGet(AbstractEventConsumer::defaultRecordHandler),
                StatusEventConsumer.class);
    }

    @Override
    protected Class<?> getKeyClass() {
        return String.class;
    }

    @Override
    protected Class<?> getValueClass() {
        return AwairStatusEvent.class;
    }   // TODO: Back to generic StatusEvent

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
    protected Optional<Supplier<Deserializer<StatusEvent>>> provideValueDeserializer() {
        return Optional.of(() -> new JsonDeserializer<StatusEvent>()
                .trustedPackages("*"));
    }

    @Override
    protected String handleKeyDeserialisationFailure(FailedDeserializationInfo failureInfo) {
        log().error("Failed to deserialise status event key [{}]", failureInfo);
        return StatusEventType.Unknown.getKey();
    }

    @Override
    protected StatusEvent handleValueDeserialisationFailure(FailedDeserializationInfo failureInfo) {
        log().error("Failed to deserialise status event value [{}]", failureInfo);
        return StatusEventError.generate(ConsumerEventError.fromFailedDeserialisationInfo(failureInfo));
    }

    private static Consumer<String, StatusEvent> defaultConsumerGenerator(Map<String, Object> consumerConfig) {
        return new KafkaConsumer<>(consumerConfig);
    }
}

