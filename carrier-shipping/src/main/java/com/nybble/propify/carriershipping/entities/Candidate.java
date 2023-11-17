package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @JsonProperty("AddressClassification")
    private AddressClassification addressClassification;

    @JsonProperty("AddressKeyFormat")
    private AddressKeyFormat addressKeyFormat;
}
