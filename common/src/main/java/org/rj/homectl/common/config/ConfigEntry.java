package org.rj.homectl.common.config;

public enum ConfigEntry {
    InputTopicName("input.topic.name");

    private final String key;
    ConfigEntry(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
