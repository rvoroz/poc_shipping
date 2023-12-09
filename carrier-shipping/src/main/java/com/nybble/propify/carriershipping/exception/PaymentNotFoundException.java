package com.nybble.propify.carriershipping.exception;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;

public class PaymentNotFoundException extends BadRequestException {
    public PaymentNotFoundException(String accountNumberNotFound) {
        super(HandleExceptionController.PAYMENT_INFO_NOT_FOUND, "Payment for accountNumber '"+ accountNumberNotFound +"' was not found");
    }
}