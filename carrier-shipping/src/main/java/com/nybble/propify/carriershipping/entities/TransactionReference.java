package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReference {

    @JsonProperty("CustomerContext")
    private String customerContext;
}
