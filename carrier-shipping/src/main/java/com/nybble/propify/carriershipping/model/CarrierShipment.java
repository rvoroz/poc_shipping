package com.nybble.propify.carriershipping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarrierShipment {
    private long id;
    private String requestId;
    private int carrier;
    private String trackingNumber;
    private String labelId;
    private String shippingLabelImage;
    private String providerLabelUrl;
}
