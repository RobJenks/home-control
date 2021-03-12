package org.rj.homectl.kafka.consumer.error;

import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

import java.util.HashMap;
import java.util.Map;

public class ErrorEventData {
    private ErrorEventType type;
    private Map<String, Object> details;

    public ErrorEventData() {
        this(ErrorEventType.Unknown);
    }

    public ErrorEventData(ErrorEventType type) {
        this(type, new HashMap<>());
    }

    public ErrorEventData(ErrorEventType type, Map<String, Object> details) {
        this.type = type;
        this.details = details;
    }

    public static ErrorEventData fromDeserializationErrorInfo(FailedDeserializationInfo failureInfo) {
        final var data = new ErrorEventData(ErrorEventType.DeserializationFailure);
        data.addDetail("topic", failureInfo.getTopic());
        data.addDetail("headers", failureInfo.getHeaders());
        data.addDetail("data", failureInfo.getData());
        data.addDetail("exception", failureInfo.getException());

        return data;
    }

    public void addDetail(String key, Object value) {
        details.put(key, value);
    }

    public ErrorEventType getType() {
        return type;
    }

    public void setType(ErrorEventType type) {
        this.type = type;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
