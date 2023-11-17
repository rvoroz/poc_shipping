package com.nybble.propify.carriershipping.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressKeyFormat {

    @JsonProperty("ConsigneeName")
    private String consigneeName;

    @JsonProperty("BuildingName")
    private String buildingName;

    @JsonProperty("AddressLine")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> addressLine;

    @JsonProperty("Region")
    private String region;

    @JsonProperty("PoliticalDivision2")
    private String politicalDivision2;

    @JsonProperty("PoliticalDivision1")
    private String politicalDivision1;

    @JsonProperty("PostcodePrimaryLow")
    private String postcodePrimaryLow;

    @JsonProperty("PostcodeExtendedLow")
    private String postcodeExtendedLow;

    @JsonProperty("Urbanization")
    private String urbanization;

    @JsonProperty("CountryCode")
    private String countryCode;

}
