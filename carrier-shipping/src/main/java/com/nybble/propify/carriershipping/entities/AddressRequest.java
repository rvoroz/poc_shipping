package com.nybble.propify.carriershipping.entities;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    @NotNull(message = "The field streetAddress is required")
    @Schema(name = "streetAddress", example = "26601 ALISO CREEK ROAD", required = true)
    private String streetAddress;

    @Schema(name = "additionalInfoAddress", example = "STE D")
    private String additionalInfoAddress;

    @Schema(name = "state", example = "CA")
    private String stateCode;

    @Schema(name = "city", example = "ALISO VIEJO")
    private String city;

    @Schema(example = "30005")
    private String postalCode;

}
