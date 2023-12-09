package com.nybble.propify.carriershipping.exception;

public class BadRequestException extends ShippingBaseRunException {

    public BadRequestException(String exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
