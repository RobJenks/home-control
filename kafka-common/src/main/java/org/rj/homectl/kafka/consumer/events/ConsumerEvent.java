package org.rj.homectl.kafka.consumer.events;

import java.time.OffsetDateTime;

public interface ConsumerEvent {

    String getType();
    void setType(String type);

    OffsetDateTime getTimestamp();
    void setTimestamp(OffsetDateTime timestamp);

}
