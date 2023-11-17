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
public class XAVResponse {

    @JsonProperty("Response")
    private Response response;

    @JsonProperty("ValidAddressIndicator")
    private String validAddressIndicator;

    @JsonProperty("AmbiguousAddressIndicator")
    private String ambiguousAddressIndicator;

    @JsonProperty("NoCandidatesIndicator")
    private String noCandidatesIndicator;

    @JsonProperty("AddressClassification")
    private AddressClassification addressClassification;

    @JsonProperty("Candidate")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Candidate> candidate;
}
