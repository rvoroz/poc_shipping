package com.nybble.propify.carriershipping.exception;

public class ShipperNotFoundException extends RuntimeException {
    public ShipperNotFoundException(String message) {
        super(message);
    }
}
