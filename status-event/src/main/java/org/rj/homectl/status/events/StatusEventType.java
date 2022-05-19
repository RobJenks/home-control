package org.rj.homectl.status.events;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEventType {
    Unknown("unknown"),
    Error("error"),
    Hue("hue"),
    ST("st"),
    Awair("awair"),
    ClusterMetrics("clusterMetrics");

    private final String key;

    StatusEventType(String key) {
        this.key = key;
    }

    @JsonValue  // Used for serde
    public String getKey() { return key; }
}
