package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.entities.CarrierShipment;
import com.nybble.propify.carriershipping.mapper.CarrierShipmentMapper;
import org.springframework.stereotype.Service;

@Service
public class CarrierShipmentService {

    private final CarrierShipmentMapper carrierShipmentMapper;

    public CarrierShipmentService(CarrierShipmentMapper carrierShipmentMapper) {
        this.carrierShipmentMapper = carrierShipmentMapper;
    }

    public void createCarrierShipment(CarrierShipment carrierShipment) {
        carrierShipmentMapper.insert(carrierShipment);
    }

}
