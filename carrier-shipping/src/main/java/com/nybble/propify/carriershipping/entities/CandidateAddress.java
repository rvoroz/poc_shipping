package com.nybble.propify.carriershipping.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateAddress {
    @Schema(example = "2311 York Rd", required = true)
    private String streetAddress;
    @Schema(example = "Alpharetta", required = true)
    private String city;
    @Schema( example = "GA", required = true)
    private String stateCode;
    @Schema(example = "30005", required = true)
    private String postalCode;
    @Schema( example = "", required = false)
    private String postalCodeExt;
    @Schema(example = "US", required = true)
    private String countryCode;
}
