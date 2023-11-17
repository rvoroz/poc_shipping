package com.nybble.propify.carriershipping.service;

import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.AddressValidationResponse;

public interface AddressService {
    AddressValidationResponse addressValidation(AddressRequest addressRequest);
}
