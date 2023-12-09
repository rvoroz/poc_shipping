package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class ServiceTypeNotFoundException extends BadRequestException {
    public ServiceTypeNotFoundException(String serviceTypeNotFound) {
        super(HandleExceptionController.SHIPMENT_SERVICE_TYPE_NOT_FOUND, "Service type for '"+ serviceTypeNotFound +"' was not found");
    }
}