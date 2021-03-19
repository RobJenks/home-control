package org.rj.homectl.status.hue;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HueEventType {
    Unknown("unknown"),
    Snapshot("snapshot"),
    Events("events");

    private final String key;

    HueEventType(String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() { return key; }
}
