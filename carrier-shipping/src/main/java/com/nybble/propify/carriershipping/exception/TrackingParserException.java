package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class TrackingParserException extends InternalErrorException {
    public TrackingParserException(Throwable cause) {
        super(HandleExceptionController.TRACKING_RESPONSE_PARSER_ERROR, "Error parsing Carrier Tracking response", cause);
    }
}
