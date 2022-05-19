package org.rj.homectl.status.metrics;

import org.rj.homectl.kafka.metrics.beans.KafkaClusterMetrics;
import org.rj.homectl.kafka.metrics.beans.KafkaConsumerMetrics;
import org.rj.homectl.status.events.StatusEventContent;


public class KafkaClusterMetricsData extends KafkaClusterMetrics implements StatusEventContent {
    public KafkaClusterMetricsData() { }


    public KafkaClusterMetricsData(KafkaConsumerMetrics consumerMetrics) {
        super(consumerMetrics);
    }
}

