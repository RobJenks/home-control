package org.rj.homectl.kafka.metrics.beans;

import java.time.OffsetDateTime;

public class KafkaClusterMetrics {
    private OffsetDateTime timestamp;
    private KafkaConsumerMetrics consumerMetrics;

    public KafkaClusterMetrics() {
        this(null);
    }

    public KafkaClusterMetrics(KafkaConsumerMetrics consumerMetrics) {
        this.timestamp = OffsetDateTime.now();
        this.consumerMetrics = consumerMetrics;
    }

    public KafkaConsumerMetrics getConsumerMetrics() {
        return consumerMetrics;
    }

    public void setConsumerMetrics(KafkaConsumerMetrics consumerMetrics) {
        this.consumerMetrics = consumerMetrics;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
