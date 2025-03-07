package com.jxmk.connection.cabinet.exception;

public class TravelOfflineException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TravelOfflineException(String message) {
        super(message);
    }

    public TravelOfflineException(Throwable cause) {
        super(cause);
    }

    public TravelOfflineException(String message, Throwable cause) {
        super(message, cause);
    }

    public TravelOfflineException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
