package com.nybble.propify.carriershipping.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.AddressValidationResponse;
import com.nybble.propify.carriershipping.service.AddressService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("api/propify/addressValidation")
public class AddressController {

    final AddressService addressService;

    AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * The Address Validation Street Level API can be used to check addresses
     * against
     * the United States Postal Service database of valid addresses in the U.S. and
     * Puerto Rico.
     * 
     * @param addressRequest streetAddress: street number, street name and street
     *                       type
     *                       State: state code
     *                       City: city name
     *                       
     * @return AddressValidationResponse
     */
    @PostMapping
    @ApiOperation(value = "Validated adress and return a list of posible candidates" , authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<AddressValidationResponse> validateAddress(@Valid @RequestBody AddressRequest addressRequest) {
        return new ResponseEntity<>(addressService.addressValidation(addressRequest), HttpStatus.OK);
    }
}
