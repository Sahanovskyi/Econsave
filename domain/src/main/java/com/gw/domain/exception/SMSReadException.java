package com.gw.domain.exception;

/**
 * Created by vadym on 30.04.17.
 */

public class SMSReadException extends Exception {
    public SMSReadException() {
        super();
    }

    public SMSReadException(String message) {
        super(message);
    }

    public SMSReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
