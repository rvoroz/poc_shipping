package com.nybble.propify.carriershipping.exception;

public class InternalErrorException extends ShippingBaseRunException {

    public InternalErrorException(String exceptionCode, String message, Throwable cause) {
        super(exceptionCode, message, cause);
    }
}
