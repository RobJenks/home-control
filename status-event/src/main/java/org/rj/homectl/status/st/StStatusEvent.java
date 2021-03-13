package org.rj.homectl.status.st;

import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.serde.StatusEventMessage;

public class StStatusEvent extends StatusEvent {
    private StStatusData data;

    public StStatusEvent() { }

    public static StStatusEvent fromMessage(StatusEventMessage message) {
        final var event = new StStatusEvent();
        event.setTimestamp(message.getTimestamp());
        event.setData(deserializeMessageDataAs(message.getData(), StStatusData.class));

        return event;
    }

    @Override
    public StatusEventType getType() {
        return StatusEventType.ST;
    }

    @Override
    public StStatusData getData() {
        return data;
    }

    public void setData(StStatusData data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
