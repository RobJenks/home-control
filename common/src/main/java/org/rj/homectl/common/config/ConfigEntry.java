package org.rj.homectl.common.config;

public enum ConfigEntry {
    InputTopicNames("input.topic.names"),
    PollDurationSec("poll.duration.secs");

    private final String key;
    ConfigEntry(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
