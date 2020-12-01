package org.rj.homectl.kafka.serialization;

import org.rj.homectl.common.beans.events.StatusEvent;

public class Serializers {

    // StatusEvent
    public static class StatusEventSerializer extends AbstractSerializer<StatusEvent> { }

}
