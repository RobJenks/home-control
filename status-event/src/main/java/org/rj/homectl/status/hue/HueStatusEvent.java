package org.rj.homectl.status.hue;

import org.rj.homectl.status.events.StatusEvent;

import java.time.OffsetDateTime;

public class HueStatusEvent extends StatusEvent<Void> {
    @Override
    public OffsetDateTime getTimestamp() {
        return null;
    }

    @Override
    public void setTimestamp(OffsetDateTime timestamp) {

    }

    @Override
    public Void getData() {
        return null;
    }
}
