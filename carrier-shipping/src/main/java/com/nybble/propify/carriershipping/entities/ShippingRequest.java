package com.nybble.propify.carriershipping.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class ShippingRequest {
    private UUID requestId;
    private ShipRequest shipFrom;
    private ShipRequest shipTo;
    @Schema(name = "Shipping Account Id", example = "C7R090", required = true)
    private String shippingAccountId;
    @Schema(name = "Service Type", example = "03", required = true)
    private String serviceType;
}
