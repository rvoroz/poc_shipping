package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.model.Carrier;
import com.nybble.propify.carriershipping.exception.CarrierNotFoundException;
import com.nybble.propify.carriershipping.mapper.CarrierMapper;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CarrierService {

    private final CarrierMapper carrierMapper;

    public CarrierService(CarrierMapper carrierMapper) {
        this.carrierMapper = carrierMapper;
    }

    public Carrier getValidCarrier(String carrierLabel) throws CarrierNotFoundException {
        Optional<Carrier> carrier = carrierMapper.findCarrierByLabel(carrierLabel.toUpperCase());
        if (carrier.isPresent()) {
            return carrier.get();
        } else {
            throw new CarrierNotFoundException(carrierLabel);
        }
    }

}
