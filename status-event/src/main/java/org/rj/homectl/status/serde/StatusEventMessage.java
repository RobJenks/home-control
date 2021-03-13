package org.rj.homectl.status.serde;

import org.rj.homectl.status.events.StatusEventType;

import java.time.OffsetDateTime;
import java.util.Map;

public class StatusEventMessage {
    private OffsetDateTime timestamp;
    private StatusEventType type;
    private Map<String, Object> data;
    private Map<String, Object> error;

    public StatusEventMessage() { }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public StatusEventType getType() {
        return type;
    }

    public void setType(StatusEventType type) {
        this.type = type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }
}
