package org.rj.homectl.status.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.rj.homectl.kafka.consumer.error.ErrorEventType;
import org.rj.homectl.status.awair.AwairStatusEvent;
import org.rj.homectl.status.error.ErrorStatusEvent;
import org.rj.homectl.status.error.ErrorStatusEventData;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.hue.HueStatusEvent;
import org.rj.homectl.status.st.StStatusEvent;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class StatusEventDeserializer extends JsonDeserializer<StatusEvent> {
    private static final Map<StatusEventType, Function<StatusEventMessage, StatusEvent>> RESOLVERS = Map.of(
            StatusEventType.Awair, AwairStatusEvent::fromMessage,
            StatusEventType.Hue, HueStatusEvent::fromMessage,
            StatusEventType.ST, StStatusEvent::fromMessage,
            StatusEventType.Error, ErrorStatusEvent::fromMessage
    );

    private final JsonDeserializer<StatusEventMessage> messageDeserializer;

    @Override
    public StatusEvent deserialize(String topic, Headers headers, byte[] data) {
        final var message = messageDeserializer.deserialize(topic, headers, data);
        return resolveMessage(message);
    }

    @Override
    public StatusEvent deserialize(String topic, byte[] data) {
        final var message = messageDeserializer.deserialize(topic, data);
        return resolveMessage(message);
    }

    private StatusEvent resolveMessage(StatusEventMessage message) {
        if (message == null) return deserializeAsEmptyMessage();

        return Optional.ofNullable(RESOLVERS.get(message.getType()))
                .map(resolver -> resolver.apply(message))
                .orElseGet(() -> deserializeAsUnknownEventClass(message));
    }

    private StatusEvent deserializeAsEmptyMessage() {
        return ErrorStatusEvent.generateErrorEvent(new ErrorStatusEventData(
                ErrorEventType.DeserializationFailure,
                Map.of("reason", "Null status event message after deserialization")));
    }

    private StatusEvent deserializeAsUnknownEventClass(StatusEventMessage message) {
        return ErrorStatusEvent.generateErrorEvent(new ErrorStatusEventData(
                ErrorEventType.DeserializationFailure,
                Map.of("reason", String.format("Unknown event class (%s)", message.getType()))));
    }

    private JsonDeserializer<StatusEventMessage> defaultMessageDeserializer() {
        return new JsonDeserializer<StatusEventMessage>()
                .trustedPackages("*");
    }

    public StatusEventDeserializer() {
        super();
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(ObjectMapper objectMapper) {
        super(objectMapper);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(Class<? super StatusEvent> targetType) {
        super(targetType);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(TypeReference<? super StatusEvent> targetType) {
        super(targetType);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(Class<? super StatusEvent> targetType, boolean useHeadersIfPresent) {
        super(targetType, useHeadersIfPresent);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(TypeReference<? super StatusEvent> targetType, boolean useHeadersIfPresent) {
        super(targetType, useHeadersIfPresent);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(Class<? super StatusEvent> targetType, ObjectMapper objectMapper) {
        super(targetType, objectMapper);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(TypeReference<? super StatusEvent> targetType, ObjectMapper objectMapper) {
        super(targetType, objectMapper);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(Class<? super StatusEvent> targetType, ObjectMapper objectMapper, boolean useHeadersIfPresent) {
        super(targetType, objectMapper, useHeadersIfPresent);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(TypeReference<? super StatusEvent> targetType, ObjectMapper objectMapper, boolean useHeadersIfPresent) {
        super(targetType, objectMapper, useHeadersIfPresent);
        this.messageDeserializer = defaultMessageDeserializer();
    }

    public StatusEventDeserializer(JavaType targetType, ObjectMapper objectMapper, boolean useHeadersIfPresent) {
        super(targetType, objectMapper, useHeadersIfPresent);
        this.messageDeserializer = defaultMessageDeserializer();
    }
}

