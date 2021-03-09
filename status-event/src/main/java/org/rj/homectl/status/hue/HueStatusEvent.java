package org.rj.homectl.status.hue;

import org.rj.homectl.status.events.StatusEvent;

import java.time.OffsetDateTime;

public class HueStatusEvent extends StatusEvent<LightsStatus> {
    private OffsetDateTime timestamp;
    private LightsStatus data;

    @Override
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public LightsStatus getData() {
        return data;
    }
}
