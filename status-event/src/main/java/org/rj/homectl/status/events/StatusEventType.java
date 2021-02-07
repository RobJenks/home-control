package org.rj.homectl.status.events;

public enum StatusEventType {
    Unknown("unknown"),
    Error("error"),
    Hue("hue"),
    ST("st"),
    Awair("awair");

    private final String key;

    StatusEventType(String key) {
        this.key = key;
    }

    public String getKey() { return key; }
}
