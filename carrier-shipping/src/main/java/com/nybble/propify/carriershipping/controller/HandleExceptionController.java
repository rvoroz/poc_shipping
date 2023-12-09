package com.nybble.propify.carriershipping.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.nybble.propify.carriershipping.exception.*;
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

@ControllerAdvice
public class HandleExceptionController extends ResponseEntityExceptionHandler {

    /**
     * ERRORS Code creation nomenclature
     * F Functionality
     * 1 = General
     * 2 = Address Validation
     * 3 = Shipment
     * 4 = Tracking
     * XXX Error Code Exception
     */
    public static final String UPS_SERVICE_GENERIC_ERROR = "1000";
    public static final String UPS_GENERIC_BAD_REQUEST_ERROR = "1001";
    public static final String CARRIER_NOT_FOUND = "1001";
    public static final String PAYMENT_INFO_NOT_FOUND = "1002";
    public static final String ADDRESS_VALIDATION_GENERIC_ERROR = "2001";
    public static final String ADDRESS_VALIDATION_REQUEST_ERROR = "2002";
    public static final String SHIPMENT_RESPONSE_PARSER_ERROR = "3001";
    public static final String SHIPMENT_SHIPPER_NOT_FOUND = "3002";
    public static final String SHIPMENT_SERVICE_TYPE_NOT_FOUND = "3003";
    public static final String SHIPMENT_SERVICE_PERSIST_CARRIER_ERROR = "3004";
    public static final String CARRIER_LABEL_NOT_FOUND = "4001";
    public static final String CARRIER_LABEL_PDF_ERROR = "4002";
    public static final String TRACKING_RESPONSE_PARSER_ERROR = "5001";

    @ExceptionHandler(value = { AddressValidationException.class })
    protected ResponseEntity<Object> handleConflict(AddressValidationException ex, WebRequest request) {
        ApiError apiError = ApiError.builder().code(ADDRESS_VALIDATION_GENERIC_ERROR).message(ex.getMessage()).build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UpsException.class })
    protected ResponseEntity<Object> handleConflict(UpsException ex, WebRequest request) {

        ApiError apiError = null;
        if (!ex.getErrors().getErrors().isEmpty()) {
            UpsResponseError upsResponseError = ex.getErrors().getErrors().get(0);
            apiError = ApiError.builder().code("UPS".concat(upsResponseError.getCode()))
                    .message(upsResponseError.getMessage()).build();
        }

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UpsProviderApiException.class })
    protected ResponseEntity<Object> handleConflict(UpsProviderApiException ex, WebRequest request) {
        return new ResponseEntity<>(getApiError(ex), HttpStatus.EXPECTATION_FAILED);
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

    @ExceptionHandler(value = { BadRequestException.class })
    protected ResponseEntity<Object> handleBadRequestNotFoundExceptions(BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(getApiError(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { InternalErrorException.class })
    protected ResponseEntity<Object> handleInternalErrorException(InternalErrorException ex, WebRequest request) {
        return new ResponseEntity<>(getApiError(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getApiError(ex), HttpStatus.NOT_FOUND);
    }

    private ApiError getApiError(ShippingBaseRunException ex) {
        return ApiError.builder().code(ex.getExceptionCode()).message(ex.getMessage()).build();
    }

}
