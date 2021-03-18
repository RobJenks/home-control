package org.rj.homectl.status.hue;

public enum HueEventType {
    Unknown("unknown"),
    Snapshot("snapshot"),
    Events("events");

    private final String key;

    HueEventType(String key) {
        this.key = key;
    }

    public String getKey() { return key; }
}
