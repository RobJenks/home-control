package org.rj.homectl.common.config;

public enum ConfigEntry {
    ConsumerId("consumer.id"),
    ConsumerConfig("consumer.config"),
    ProducerId("producer.id"),
    ProducerConfig("producer.config"),
    InputTopicNames("input.topic.names"),
    PollDurationSec("poll.duration.secs"),
    MonitorSensors("monitor.sensors"),
    MonitorQuery("monitor.query"),
    MonitorPollIntervalMs("monitor.pollIntervalMs");

    private final String key;
    ConfigEntry(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
