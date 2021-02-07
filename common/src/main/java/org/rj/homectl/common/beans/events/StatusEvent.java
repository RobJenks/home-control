package org.rj.homectl.common.beans.events;

import java.time.OffsetDateTime;

public abstract class StatusEvent {
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
