package org.rj.homectl.consumer.status.events;

import org.rj.homectl.kafka.consumer.events.AbstractConsumerEvent;

import java.time.OffsetDateTime;

public abstract class StatusEvent extends AbstractConsumerEvent {
    private String type;
    private OffsetDateTime timestamp;

    public StatusEvent() { }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
