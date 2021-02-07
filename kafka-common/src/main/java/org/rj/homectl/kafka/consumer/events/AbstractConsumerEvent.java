package org.rj.homectl.kafka.consumer.events;

import org.rj.homectl.kafka.consumer.error.ConsumerEventError;

public abstract class AbstractConsumerEvent implements ConsumerEvent {
    private ConsumerEventError error;

    public boolean isValid() {
        return !isInError();
    }

    public boolean isInError() {
        return (error != null);
    }

    public ConsumerEventError getError() {
        return error;
    }

    public void setError(ConsumerEventError error) {
        this.error = error;
    }
}
