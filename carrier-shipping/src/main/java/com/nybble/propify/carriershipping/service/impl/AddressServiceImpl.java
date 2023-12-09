package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.mapper.repository.AddressRepository;
import org.springframework.stereotype.Service;

import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.AddressValidationResponse;
import com.nybble.propify.carriershipping.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    final AddressRepository addressRepository;

    AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressValidationResponse addressValidation(AddressRequest addressRequest) {
        return addressRepository.adddressValidate(addressRequest);
    }

}
