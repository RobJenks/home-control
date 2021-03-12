package org.rj.homectl.kafka.consumer.error;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorEventType {
    Unknown("unknown"),
    DeserializationFailure("deserializationFailure"),
    ProducerFailure("producerFailure");

    private final String key;

    ErrorEventType(String key) {
        this.key = key;
    }

    @JsonValue  // used for serde
    public String getKey() {
        return key;
    }
}
