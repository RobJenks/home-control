package org.rj.homectl.status.events;

import org.rj.homectl.common.util.Util;
import org.rj.homectl.kafka.consumer.events.ConsumerEvent;
import org.rj.homectl.status.error.StatusEventProcessingException;

import java.time.OffsetDateTime;
import java.util.Map;


public abstract class StatusEvent implements ConsumerEvent {
    private OffsetDateTime timestamp;

    public StatusEvent() { }

    @Override
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public abstract StatusEventType getType();

    public abstract boolean isValid();
    public boolean isError() { return !isValid(); }

    public abstract StatusEventContent getData();

    protected static <T extends StatusEventContent> T deserializeMessageDataAs(Map<String, Object> data, Class<T> cls) {
        if (data == null) return null;
        if (cls == null) throw new StatusEventProcessingException("Failed to deserialize message content; no target class provided");

        try {
            return Util.objectMapper().convertValue(data, cls);
        }
        catch (Throwable t) {
            throw new StatusEventProcessingException(String.format(
                    "Failed to deserialize message data to \"%s\" (data: %s, error: %s)", cls.getName(), Util.safeSerialize(data), t));
        }
    }


}
