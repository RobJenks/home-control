package org.rj.homectl.status.hue;

import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.serde.StatusEventMessage;


public class HueStatusEvent extends StatusEvent {
    private HueStatusData data;

    public HueStatusEvent() { }

    public static HueStatusEvent fromMessage(StatusEventMessage message) {
        final var event = new HueStatusEvent();
        event.setTimestamp(message.getTimestamp());
        event.setData(deserializeMessageDataAs(message.getData(), HueStatusData.class));

        return event;
    }

    @Override
    public StatusEventType getType() {
        return StatusEventType.Hue;
    }

    @Override
    public HueStatusData getData() {
        return data;
    }

    public void setData(HueStatusData data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}