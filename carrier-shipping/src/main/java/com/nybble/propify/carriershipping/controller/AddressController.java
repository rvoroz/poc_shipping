package com.nybble.propify.carriershipping.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.AddressValidationResponse;
import com.nybble.propify.carriershipping.service.AddressService;

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
     * @param addressRequest Address line (street number, street name and street
     *                       type, and political division 1, political division 2
     *                       and postal code) used for street level information.
     *                       Additional secondary information (apartment, suite,
     *                       floor, etc.) Applicable to US and PR only
     *                       Eg: ["26601 ALISO CREEK ROAD","STE D","ALISO VIEJO TOWN
     *                       CENTER", "CA"]
     * @return AddressValidationResponse
     */
    @PostMapping
    public AddressValidationResponse validateAddress(@Valid @RequestBody AddressRequest addressRequest) {
        return addressService.addressValidation(addressRequest);
    }
}
