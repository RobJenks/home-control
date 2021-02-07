package org.rj.homectl.status.errors;

import org.rj.homectl.kafka.consumer.error.ConsumerEventError;
import org.rj.homectl.status.events.StatusEvent;
import org.rj.homectl.status.events.StatusEventType;

import java.time.OffsetDateTime;

public class StatusEventError extends StatusEvent {
    private ConsumerEventError error;

    public StatusEventError() { }

    public static StatusEventError generate(ConsumerEventError error) {
        final var event = new StatusEventError();
        event.setType(StatusEventType.Error.getKey());
        event.setTimestamp(OffsetDateTime.now());
        event.setError(error);

        return event;
    }

    public ConsumerEventError getError() {
        return error;
    }

    public void setError(ConsumerEventError error) {
        this.error = error;
    }
}
