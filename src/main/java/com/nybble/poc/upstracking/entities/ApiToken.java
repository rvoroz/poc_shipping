package com.nybble.poc.upstracking.entities;

import lombok.Data;

@Data
public class ApiToken {
    private Long id;
    private String token;
    private String description;
}
