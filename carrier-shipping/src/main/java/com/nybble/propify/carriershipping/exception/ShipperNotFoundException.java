package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class ShipperNotFoundException extends BadRequestException {
    public ShipperNotFoundException(String shipperNumberNotFound) {
        super(HandleExceptionController.SHIPMENT_SHIPPER_NOT_FOUND, "Shipper for shipperNumber '"+ shipperNumberNotFound +"' was not found");
    }
}