package org.rj.homectl.status.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.rj.homectl.kafka.common.KafkaConstants;
import org.rj.homectl.status.error.ErrorStatusEvent;
import org.rj.homectl.status.error.ErrorStatusEventData;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.common.config.Config;
import org.rj.homectl.kafka.consumer.AbstractEventConsumer;
import org.rj.homectl.kafka.consumer.ConsumerGenerator;
import org.rj.homectl.kafka.consumer.handlers.ConsumerRecordsHandler;
import org.rj.homectl.status.serde.StatusEventDeserializer;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class StatusEventConsumer extends AbstractEventConsumer<String, StatusEvent> {

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
        return StatusEventDeserializer.class;
    }

    @Override
    protected Optional<Supplier<Deserializer<String>>> provideKeyDeserializer() {
        return Optional.empty();
    }

    @Override
    protected Optional<Supplier<Deserializer<StatusEvent>>> provideValueDeserializer() {
        return Optional.of(() -> new StatusEventDeserializer()
                .trustedPackages("*"));
    }

    @Override
    protected String handleKeyDeserializationFailure(FailedDeserializationInfo failureInfo) {
        log().error("Failed to deserialize status event key [{}]", failureInfo);
        return KafkaConstants.KEY_UNKNOWN;
    }

    @Override
    protected StatusEvent handleValueDeserializationFailure(FailedDeserializationInfo failureInfo) {
        log().error("Failed to deserialize status event value [{}]", failureInfo);

        return ErrorStatusEvent.generateErrorEvent((ErrorStatusEventData)
                ErrorStatusEventData.fromDeserializationErrorInfo(failureInfo));

    }

    public Map<MetricName, ? extends Metric> getConsumerMetrics() {
        return super.getConsumerMetrics();
    }

    private static <T> Consumer<String, StatusEvent> defaultConsumerGenerator(Map<String, Object> consumerConfig) {
        return new KafkaConsumer<>(consumerConfig);
    }
}

