package com.nybble.propify.carriershipping.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AddressValidationResponse {

    private List<CandidateAddress> candidates;

    public AddressValidationResponse() {
        candidates = new ArrayList<>();
    }
}
