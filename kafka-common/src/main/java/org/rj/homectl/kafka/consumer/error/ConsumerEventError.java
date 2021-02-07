package org.rj.homectl.kafka.consumer.error;

import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

import java.util.Optional;

public class ConsumerEventError {
    private String topic;
    private Headers headers;
    private byte[] data;
    private Exception exception;

    public ConsumerEventError() { }

    public String getDataAsString() {
        return new String(data);
    }

    @Override
    public String toString() {
        return "ConsumerEventError (" + Optional.ofNullable(exception).map(Exception::toString).orElse("<null>") + ")";
    }

    public static ConsumerEventError fromFailedDeserialisationInfo(FailedDeserializationInfo failure) {
        final var error = new ConsumerEventError();
        error.setTopic(failure.getTopic());
        error.setHeaders(failure.getHeaders());
        error.setData(failure.getData());
        error.setException(failure.getException());

        return error;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
