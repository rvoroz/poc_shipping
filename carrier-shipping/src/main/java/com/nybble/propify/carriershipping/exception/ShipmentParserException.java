package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class ShipmentParserException extends InternalErrorException {
    public ShipmentParserException(Throwable cause) {
        super(HandleExceptionController.SHIPMENT_RESPONSE_PARSER_ERROR, "Error parsing Carrier response", cause);
    }
}
