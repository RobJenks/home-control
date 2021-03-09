package org.rj.homectl.status.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rj.homectl.kafka.consumer.error.ConsumerEventError;
import org.rj.homectl.kafka.consumer.events.AbstractConsumerEvent;


public abstract class StatusEvent<T> extends AbstractConsumerEvent {
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ConsumerEventError error;

    public StatusEvent() { }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public ConsumerEventError getError() { return error; }
    public void setError(ConsumerEventError error) { this.error = error; }

    public boolean isValid() { return (error == null); }
    public boolean isError() { return !isValid(); }

    public abstract T getData();

}
