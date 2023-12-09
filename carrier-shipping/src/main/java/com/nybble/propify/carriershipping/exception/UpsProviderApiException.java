package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class UpsProviderApiException extends ShippingBaseRunException {

    public UpsProviderApiException(String message) {
        super(HandleExceptionController.UPS_SERVICE_GENERIC_ERROR, message);
    }
    public UpsProviderApiException(String message, Throwable cause) {
        super(HandleExceptionController.UPS_SERVICE_GENERIC_ERROR, message, cause);
    }

}
