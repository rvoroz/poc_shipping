package com.nybble.propify.carriershipping.mapper.repository;

import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse;
import com.nybble.propify.carriershipping.entities.UpsShipmentRequest;
import com.nybble.propify.carriershipping.exception.ShipmentParserException;
import org.springframework.stereotype.Service;

import com.nybble.propify.carriershipping.provider.UPSProviderApi;


@Service
public class UpsShippingRepository {

    private UPSProviderApi upsProviderApi;

    public UpsShippingRepository(UPSProviderApi upsProviderApi) {
        this.upsProviderApi = upsProviderApi;
    }

    public UpsShipmentApiResponse generateShipment(String transactionId, UpsShipmentRequest shipmentRequest) throws ShipmentParserException {
        return upsProviderApi.shipment(transactionId, shipmentRequest);
    }
}
