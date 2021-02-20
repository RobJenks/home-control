package org.rj.homectl.status.errors;

import org.rj.homectl.kafka.consumer.error.ConsumerEventError;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;

import java.time.OffsetDateTime;

public class StatusEventError extends StatusEvent<ConsumerEventError> {
    private OffsetDateTime timestamp;
    private ConsumerEventError error;

    public StatusEventError() { }

    public static StatusEventError generate(ConsumerEventError error) {
        final var event = new StatusEventError();
        event.setType(StatusEventType.Error.getKey());
        event.setTimestamp(OffsetDateTime.now());
        event.setData(error);

        return event;
    }

    @Override
    public ConsumerEventError getData() {
        return error;
    }

    public void setData(ConsumerEventError error) {
        this.error = error;
    }

    @Override
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
