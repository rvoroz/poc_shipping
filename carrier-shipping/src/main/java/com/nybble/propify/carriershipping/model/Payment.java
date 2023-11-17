package com.nybble.propify.carriershipping.model;

import lombok.Data;

@Data
public class Payment {
    private Long id;
    private String type;
    private String accountNumber;
}
