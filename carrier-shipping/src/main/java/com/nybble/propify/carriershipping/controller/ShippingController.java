package com.nybble.propify.carriershipping.controller;

import com.nybble.propify.carriershipping.dto.ShippingRequest;
import com.nybble.propify.carriershipping.dto.CarrierShippingResponse;
import com.nybble.propify.carriershipping.service.impl.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
     * The Generate Tracking Number And Shipping Label API will generate Tracking Number And Shipping Label
     * for a ship.
     *
     * @param shippingRequest
     * @return ShippingResponse
     */
    @PostMapping("/generateLabel/{carrier}")
    public  ResponseEntity<CarrierShippingResponse> generateTrackingNumberAndShippingLabel(@PathVariable("carrier") String carrier, @RequestBody ShippingRequest shippingRequest){
        generateTransactionRequestId(shippingRequest);
        log.info("GenerateTrackingNumberAndShippingLabel - START - Request {} - {} carrier ", shippingRequest.getRequestId(),carrier.toUpperCase());
        CarrierShippingResponse carrierShippingResponse = shippingService.processShipmentGeneration(carrier, shippingRequest);
        log.info("GenerateTrackingNumberAndShippingLabel - END - Request {} - {} carrier ", shippingRequest.getRequestId(), carrier.toUpperCase());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(carrierShippingResponse, headers, HttpStatus.OK);
    }

    private void generateTransactionRequestId(ShippingRequest request) {
        if (Objects.isNull(request.getRequestId())){
            request.setRequestId(UUID.randomUUID());
        }
    }
}
