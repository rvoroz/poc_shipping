package com.nybble.propify.carriershipping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class TrackingDetailResponse {
    private UUID requestId;
    private String carrier;
    private String requestTrackingNumber;
    private List<Shipment> shipments;


    @Data
    @Builder
    public static class Shipment {
        private String trackingNumber;
        private String deliveryDate;
        private String deliveryTime;
        //   private Date delivery; time
        private String statusCode;
        private String statusCodeStatus;
        private String statusDescription;
    }

}
