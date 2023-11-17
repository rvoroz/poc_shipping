package com.nybble.propify.carriershipping.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateAddress {
    private String streetAddress;
    private String additionalInfoAddress;
    private String city;
    private String stateCode;
    private String postalCode;
    private String postalCodeExt;
    private String countryCode;
}
