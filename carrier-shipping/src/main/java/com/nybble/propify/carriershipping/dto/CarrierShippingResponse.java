package com.nybble.propify.carriershipping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class CarrierShippingResponse {
    private UUID requestId;
    private String carrier;
    private String trackingNumber;
    private String labelUrl;
}
