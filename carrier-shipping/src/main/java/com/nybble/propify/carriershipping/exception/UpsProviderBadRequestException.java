package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class UpsProviderBadRequestException extends BadRequestException {

    public UpsProviderBadRequestException(String message) {
        super(HandleExceptionController.UPS_GENERIC_BAD_REQUEST_ERROR, message);
    }

}
