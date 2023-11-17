package com.nybble.propify.carriershipping.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsResponse {
    private List<UpsResponseError> errors;
}
