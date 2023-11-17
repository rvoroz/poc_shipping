package com.nybble.propify.carriershipping.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShipRequest {

    private String name;
    private CandidateAddress address;

}
