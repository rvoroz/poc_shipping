package com.nybble.propify.carriershipping.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShipRequest {

    @Schema(example = "T and T Designs", required = true)
    private String name;
    private CandidateAddress address;

}
