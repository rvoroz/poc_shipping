package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class CarrierLabelNotFoundException extends NotFoundException {
    public CarrierLabelNotFoundException(String carrierLabelNotFound) {
        super(HandleExceptionController.CARRIER_LABEL_NOT_FOUND, "Label ID '"+ carrierLabelNotFound +"' was not found");
    }
}