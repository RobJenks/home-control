package org.rj.homectl.common.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceClass {
    Unknown("unknown"),
    Awair("awair"),
    Hue("hue"),
    St("st");

    private final String key;
    DeviceClass(final String key) {
        this.key = key;
    }

    @JsonValue  // Used for serde
    public String getKey() {
        return key;
    }
}
