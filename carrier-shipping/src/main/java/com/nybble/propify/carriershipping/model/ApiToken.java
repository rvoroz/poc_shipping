package com.nybble.propify.carriershipping.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiToken {
    private Long id;
    private String token;
    private String description;
}
