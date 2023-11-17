package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XAVRequest {
    @JsonProperty("Request")
    private Request request;

    @JsonProperty("AddressKeyFormat")
    private AddressKeyFormat addressKeyFormat;
}
