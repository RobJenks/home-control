package org.rj.homectl.consumer.status;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.rj.homectl.consumer.status.events.StatusEvent;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.consumer.status.awair.AwairStatusEvent;
import org.rj.homectl.kafka.consumer.AbstractEventConsumer;
import org.rj.homectl.kafka.consumer.ConsumerGenerator;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
                               final ConsumerRecordsHandler<String, StatusEvent> recordsHandler) {
        super(id, config, consumerGenerator.orElse(StatusEventConsumer::defaultConsumerGenerator), recordsHandler, StatusEventConsumer.class);
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

    private static Consumer<String, StatusEvent> defaultConsumerGenerator(Map<String, Object> consumerConfig) {
        return new KafkaConsumer<>(consumerConfig);
    }
}

