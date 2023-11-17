package com.nybble.propify.carriershipping.exception;

public class CarrierNotFoundException extends RuntimeException {
    public CarrierNotFoundException(String message) {
        super(message);
    }
}
