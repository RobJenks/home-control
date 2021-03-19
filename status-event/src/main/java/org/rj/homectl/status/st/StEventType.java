package org.rj.homectl.status.st;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StEventType {
    Unknown("unknown"),
    Snapshot("snapshot"),
    Events("events");

    private final String key;

    StEventType(final String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}
