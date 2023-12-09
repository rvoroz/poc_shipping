package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class AddressValidationRequestException extends BadRequestException {
    public AddressValidationRequestException() {
        super(HandleExceptionController.ADDRESS_VALIDATION_REQUEST_ERROR,
                "Invalid request. There should be at least one of the following fields: stateCode, city, postalCode");
    }
}
