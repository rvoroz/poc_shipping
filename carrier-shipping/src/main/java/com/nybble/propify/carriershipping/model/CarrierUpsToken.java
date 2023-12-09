package com.nybble.propify.carriershipping.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CarrierUpsToken {
    private Long id;
    private String token;
    private Date expiredDate;
}
