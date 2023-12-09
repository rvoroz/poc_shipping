package com.nybble.propify.carriershipping.controller;

import com.nybble.propify.carriershipping.entities.ShippingRequest;
import com.nybble.propify.carriershipping.entities.CarrierShippingResponse;
import com.nybble.propify.carriershipping.service.impl.ShippingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

@RequestMapping("/api/propify/shipping")
@RestController
@Slf4j
public class ShippingController {

    ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    /**
     * This API will generate Tracking Number And Shipping Label for a ship for assigned carrier.
     *
     * @param shippingRequest
     * @return ShippingResponse
     */
    @Operation(summary = "Generate Tracking Number And Get Shipping label url",
            description= "This API will generate Tracking Number And Shipping Label for a ship for assigned carrier."
    )
    @ApiOperation( value = "This API will generate Tracking Number And Shipping Label for a ship for assigned carrier."
            ,  authorizations = @Authorization(value = "Bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "417", description = "Expectation Failed: unexpected error integration with third party"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })


    @PostMapping("/{carrier}/generate")
    public  ResponseEntity<CarrierShippingResponse> generateTrackingNumberAndShippingLabel(@PathVariable("carrier") @Parameter(example = "ups") String carrier, @RequestBody ShippingRequest shippingRequest){
        generateTransactionRequestId(shippingRequest);
        log.info("GenerateTrackingNumberAndShippingLabel - START - Request {} - {} carrier ", shippingRequest.getRequestId(),carrier.toUpperCase());
        CarrierShippingResponse carrierShippingResponse = shippingService.processShipmentGeneration(carrier, shippingRequest);
        log.info("GenerateTrackingNumberAndShippingLabel - END - Request {} - {} carrier ", shippingRequest.getRequestId(), carrier.toUpperCase());
        return new ResponseEntity<>(carrierShippingResponse, HttpStatus.CREATED);
    }

    private void generateTransactionRequestId(ShippingRequest request) {
        if (Objects.isNull(request.getRequestId())){
            request.setRequestId(UUID.randomUUID());
        }
    }
}
