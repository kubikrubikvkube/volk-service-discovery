package com.github.kubikrubikvkube.volkservicediscovery.exception;

public class VolkRuntimeException extends RuntimeException {
    public VolkRuntimeException() {
        super();
    }

    public VolkRuntimeException(String message) {
        super(message);
    }

    public VolkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public VolkRuntimeException(Throwable cause) {
        super(cause);
    }

    protected VolkRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
