package com.nybble.propify.carriershipping.entities;

import lombok.Data;

@Data
public class ApiToken {
    private Long id;
    private String token;
    private String description;
}
