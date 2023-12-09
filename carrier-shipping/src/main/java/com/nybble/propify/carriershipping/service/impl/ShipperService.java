package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.exception.ShipperNotFoundException;
import com.nybble.propify.carriershipping.mapper.ShipperMapper;
import com.nybble.propify.carriershipping.model.Shipper;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ShipperService {

    private final ShipperMapper shipperMapper;

    public ShipperService(ShipperMapper shipperMapper) {
        this.shipperMapper = shipperMapper;
    }

    public Shipper getShipperInfoByShipperNumber(String shipperNumber) throws ShipperNotFoundException {
        Optional<Shipper> defaultShipper = shipperMapper.findShipperInfoByShipperNumber(shipperNumber);
        if (defaultShipper.isPresent()) {
            return defaultShipper.get();
        } else {
            throw new ShipperNotFoundException(shipperNumber);
        }
    }

}
