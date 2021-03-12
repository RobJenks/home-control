package org.rj.homectl.status.error;

import org.rj.homectl.kafka.consumer.error.ErrorEventData;
import org.rj.homectl.kafka.consumer.error.ErrorEventType;
import org.rj.homectl.status.events.StatusEventContent;

import java.util.Map;

public class ErrorStatusEventData extends ErrorEventData
                                  implements StatusEventContent {

    public ErrorStatusEventData() {
    }

    public ErrorStatusEventData(ErrorEventType type) {
        super(type);
    }

    public ErrorStatusEventData(ErrorEventType type, Map<String, Object> details) {
        super(type, details);
    }
}
