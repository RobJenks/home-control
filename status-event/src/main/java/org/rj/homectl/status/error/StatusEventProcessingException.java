package org.rj.homectl.status.error;

public class StatusEventProcessingException extends RuntimeException {
    public StatusEventProcessingException() {
    }

    public StatusEventProcessingException(String message) {
        super(message);
    }

    public StatusEventProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusEventProcessingException(Throwable cause) {
        super(cause);
    }

    public StatusEventProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
