package org.rj.homectl.status.awair;

import org.rj.homectl.awair.model.AwairStatus;
import org.rj.homectl.status.events.StatusEvent;

import java.time.OffsetDateTime;

public class AwairStatusEvent extends StatusEvent<AwairStatus> {
    private AwairStatus data;

    public AwairStatusEvent() { }

    @Override
    public OffsetDateTime getTimestamp() {
        return (data != null ? data.getTimestamp() : null);
    }

    @Override
    public void setTimestamp(OffsetDateTime timestamp) {
        if (data != null) {
            data.setTimestamp(timestamp);
        }
    }

    @Override
    public AwairStatus getData() {
        return data;
    }
}
