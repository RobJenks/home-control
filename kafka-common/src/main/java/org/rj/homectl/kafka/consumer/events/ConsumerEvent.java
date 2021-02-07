package org.rj.homectl.kafka.consumer.events;

import org.rj.homectl.kafka.consumer.error.ConsumerEventError;

import java.util.Optional;

public interface ConsumerEvent {

    boolean isValid();
    boolean isInError();

    ConsumerEventError getError();
    void setError(ConsumerEventError error);
    default Optional<ConsumerEventError> getErrorOptional() { return Optional.ofNullable(getError()); }

}
