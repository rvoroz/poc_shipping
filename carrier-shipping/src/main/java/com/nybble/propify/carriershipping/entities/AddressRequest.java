package com.nybble.propify.carriershipping.entities;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    @NotNull(message = "The field addressLine is required")
    private List<String> addressLine;

    @NotNull(message = "The field state is required")
    private String state;

    @NotNull(message = "The field city is required")
    private String city;
}
