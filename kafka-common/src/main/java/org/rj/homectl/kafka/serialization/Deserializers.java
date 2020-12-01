package org.rj.homectl.kafka.serialization;

import org.rj.homectl.common.beans.events.StatusEvent;

public class Deserializers {

    // StatusEvent
    public static class StatusEventDeserializer extends AbstractDeserializer<StatusEvent> {
        public StatusEventDeserializer() { super(StatusEvent.class); }
    }
}

