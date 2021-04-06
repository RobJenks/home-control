package org.rj.homectl.status.hue;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HueEventType {
    Unknown("unknown"),
    Snapshot("snapshot"),
    Update("update"),
    Event("event");

    private final String key;

    HueEventType(final String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() { return key; }
}
