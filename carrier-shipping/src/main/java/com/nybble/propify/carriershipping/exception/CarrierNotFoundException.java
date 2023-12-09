package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class CarrierNotFoundException extends BadRequestException {
    public CarrierNotFoundException(String carrierNotFound) {
        super(HandleExceptionController.CARRIER_NOT_FOUND, "Carrier with label '"+ carrierNotFound +"' was not found");
    }
}
