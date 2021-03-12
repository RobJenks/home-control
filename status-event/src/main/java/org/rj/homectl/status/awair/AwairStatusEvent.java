package org.rj.homectl.status.awair;

import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.serde.StatusEventMessage;

public class AwairStatusEvent extends StatusEvent {
    private AwairStatusData data;

    public AwairStatusEvent() { }

    public static AwairStatusEvent fromMessage(StatusEventMessage message) {
        final var event = new AwairStatusEvent();
        event.setTimestamp(message.getTimestamp());
        event.setData(deserializeMessageDataAs(message.getData(), AwairStatusData.class));

        return event;
    }

    @Override
    public StatusEventType getType() {
        return StatusEventType.Awair;
    }

    @Override
    public AwairStatusData getData() {
        return data;
    }

    public void setData(AwairStatusData data) {
        this.data = data;
    }

    @Override
    public boolean isValid() { return true; }
}
