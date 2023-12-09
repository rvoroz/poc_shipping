package com.nybble.propify.carriershipping.exception;

import lombok.Getter;

public class ShippingBaseRunException extends RuntimeException {

    @Getter
    protected String exceptionCode;
    public ShippingBaseRunException(String exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ShippingBaseRunException(String exceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}
