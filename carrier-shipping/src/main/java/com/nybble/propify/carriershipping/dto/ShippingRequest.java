package com.nybble.propify.carriershipping.dto;

import com.nybble.propify.carriershipping.entities.ShipRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class ShippingRequest {

    private UUID requestId;
    private ShipRequest shipFrom; //Todo replace with AddressStandaraze
    private ShipRequest shipTo;   //Todo replace with AddressStandaraze
    private String shippingAccountId; //TODO implement on request
    private String serviceType;
}
