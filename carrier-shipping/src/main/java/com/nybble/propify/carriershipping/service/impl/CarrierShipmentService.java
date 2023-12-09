package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.controller.HandleExceptionController;
import com.nybble.propify.carriershipping.model.CarrierShipment;
import com.nybble.propify.carriershipping.exception.InternalErrorException;
import com.nybble.propify.carriershipping.mapper.CarrierShipmentMapper;
import org.springframework.stereotype.Service;

@Service
public class CarrierShipmentService {

    private final CarrierShipmentMapper carrierShipmentMapper;

    public CarrierShipmentService(CarrierShipmentMapper carrierShipmentMapper) {
        this.carrierShipmentMapper = carrierShipmentMapper;
    }

    public void createCarrierShipment(CarrierShipment carrierShipment) {
        try {
            carrierShipmentMapper.insert(carrierShipment);
        } catch (Exception e) {
            throw new InternalErrorException(HandleExceptionController.SHIPMENT_SERVICE_PERSIST_CARRIER_ERROR,
                    "Shipment could not be saved", e);
        }
    }

}
