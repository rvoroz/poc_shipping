package com.nybble.propify.carriershipping.model;

import lombok.Data;

@Data
public class Shipper {
    private Long id;
    private String name;
    private String shipperNumber;
    private String streetAddress;
    private String additionalInfoAddress;
    private String city;
    private String countryCode;
    private String postalCode;
    private String stateCode ;
}
