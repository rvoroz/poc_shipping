package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class PDFGenerationException extends InternalErrorException {
    public PDFGenerationException(Throwable cause) {
        super(HandleExceptionController.CARRIER_LABEL_PDF_ERROR, "Error creating PDF label", cause);
    }
}
