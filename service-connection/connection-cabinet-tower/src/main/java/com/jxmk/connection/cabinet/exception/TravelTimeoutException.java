package com.jxmk.connection.cabinet.exception;

public class TravelTimeoutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TravelTimeoutException(String message) {
        super(message);
    }

    public TravelTimeoutException(Throwable cause) {
        super(cause);
    }

    public TravelTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TravelTimeoutException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
