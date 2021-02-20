package org.rj.homectl.status.events;

import org.rj.homectl.kafka.consumer.events.AbstractConsumerEvent;


public abstract class StatusEvent<T> extends AbstractConsumerEvent {
    private String type;

    public StatusEvent() { }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public abstract T getData();

}
