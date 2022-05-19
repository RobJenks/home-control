package org.rj.homectl.status.metrics;

import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.serde.StatusEventMessage;

public class KafkaClusterMetricsStatusEvent extends StatusEvent {
    private KafkaClusterMetricsData data;

    public KafkaClusterMetricsStatusEvent() { }

    public static KafkaClusterMetricsStatusEvent fromMessage(StatusEventMessage message) {
        final var event = new KafkaClusterMetricsStatusEvent();
        event.setTimestamp(message.getTimestamp());
        event.setData(deserializeMessageDataAs(message.getData(), KafkaClusterMetricsData.class));

        return event;
    }

    @Override
    public StatusEventType getType() {
        return StatusEventType.ClusterMetrics;
    }

    @Override
    public KafkaClusterMetricsData getData() {
        return data;
    }

    public void setData(KafkaClusterMetricsData data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
