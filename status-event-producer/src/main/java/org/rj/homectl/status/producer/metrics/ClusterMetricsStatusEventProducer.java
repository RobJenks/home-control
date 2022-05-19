package org.rj.homectl.status.producer.metrics;

import org.rj.homectl.common.config.Config;
import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.producer.ProducerGenerator;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.metrics.KafkaClusterMetricsData;
import org.rj.homectl.status.producer.StatusEventProducer;
import org.rj.homectl.status.serde.StatusEventMessage;

import java.util.Map;

public class ClusterMetricsStatusEventProducer extends StatusEventProducer<KafkaClusterMetricsData> {

    public ClusterMetricsStatusEventProducer(String id, Config config, ProducerGenerator<String, StatusEventMessage> producerGenerator) {
        super(id, config, producerGenerator, ClusterMetricsStatusEventProducer.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StatusEventMessage prepareMessage(KafkaClusterMetricsData data) {
        final var message = new StatusEventMessage();
        message.setTimestamp(data.getTimestamp());
        message.setType(StatusEventType.ClusterMetrics);
        message.setData(Util.objectMapper().convertValue(data, Map.class));

        return message;
    }
}
