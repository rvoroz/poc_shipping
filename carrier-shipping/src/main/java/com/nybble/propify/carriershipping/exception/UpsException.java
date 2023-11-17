package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.entities.UpsResponse;

public class UpsException extends RuntimeException {

    private UpsResponse errors;

    
    public UpsException(String message) {
        super(message);
    }

    public UpsException(UpsResponse upsResponse) {
        this.errors = upsResponse;
    }

    public UpsResponse getErrors() {
        return this.errors;
    }

}
