package com.nybble.propify.carriershipping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nybble.propify.carriershipping.entities.ApiError;
import com.nybble.propify.carriershipping.entities.UpsResponseError;
import com.nybble.propify.carriershipping.exception.AddressValidationException;
import com.nybble.propify.carriershipping.exception.UpsException;
import com.nybble.propify.carriershipping.exception.UpsProviderApiException;

@ControllerAdvice
public class HandleExceptionController extends ResponseEntityExceptionHandler {

    public static String UPS_SERVICE_GENERIC_ERROR = "1000";
    public static String ADDRESS_VALIDATION_GENERIC_ERROR = "1001";
    public static String ADDRESS_VALIDATION_REQUEST_ERROR = "1002";

    @ExceptionHandler(value = { AddressValidationException.class })
    protected ResponseEntity<Object> handleConflict(
            AddressValidationException ex, WebRequest request) {
        ApiError apiError = ApiError.builder()
                .code(ADDRESS_VALIDATION_GENERIC_ERROR).message(ex.getMessage()).build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UpsException.class })
    protected ResponseEntity<Object> handleConflict(
            UpsException ex, WebRequest request) {

        ApiError apiError = null;
        if (!ex.getErrors().getErrors().isEmpty()) {
            UpsResponseError upsResponseError = ex.getErrors().getErrors().get(0);
            apiError = ApiError.builder().code("UPS".concat(upsResponseError.getCode()))
                    .message(upsResponseError.getMessage()).build();
        }

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UpsProviderApiException.class })
    protected ResponseEntity<Object> handleConflict(
            UpsProviderApiException ex, WebRequest request) {

        ApiError apiError = ApiError.builder().code(UPS_SERVICE_GENERIC_ERROR).message(ex.getMessage()).build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder().code(ADDRESS_VALIDATION_REQUEST_ERROR).message(errors.get(0)).build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
