package org.rj.homectl.status.error;

import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;
import org.rj.homectl.status.serde.StatusEventMessage;
import org.rj.homectl.status.st.StStatusData;
import org.rj.homectl.status.st.StStatusEvent;

import java.time.OffsetDateTime;

public class ErrorStatusEvent extends StatusEvent {
    private ErrorStatusEventData data;

    public ErrorStatusEvent() { }

    public static ErrorStatusEvent generateErrorEvent(ErrorStatusEventData data) {
        final var event = new ErrorStatusEvent();
        event.setTimestamp(OffsetDateTime.now());
        event.setData(data);

        return event;
    }

    public static ErrorStatusEvent fromMessage(StatusEventMessage message) {
        final var event = new ErrorStatusEvent();
        event.setTimestamp(message.getTimestamp());
        event.setData(deserializeMessageDataAs(message.getData(), ErrorStatusEventData.class));

        return event;
    }

    @Override
    public StatusEventType getType() {
        return StatusEventType.Error;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public ErrorStatusEventData getData() {
        return data;
    }

    public void setData(ErrorStatusEventData data) {
        this.data = data;
    }
}
