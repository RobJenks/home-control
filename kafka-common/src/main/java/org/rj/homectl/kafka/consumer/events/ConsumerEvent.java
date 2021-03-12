package org.rj.homectl.kafka.consumer.events;

import java.time.OffsetDateTime;

public interface ConsumerEvent {

    OffsetDateTime getTimestamp();
    void setTimestamp(OffsetDateTime timestamp);

}
