package com.nybble.propify.carriershipping.exception;

public class NotFoundException extends ShippingBaseRunException {

    public NotFoundException(String exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
