package com.nybble.propify.carriershipping.mapper.repository;

import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse;
import com.nybble.propify.carriershipping.entities.UpsShipmentRequest;
import com.nybble.propify.carriershipping.exception.ShippingLabelException;
import org.springframework.stereotype.Service;

import com.nybble.propify.carriershipping.provider.UPSProviderApi;


@Service
public class UpsShippingRepository {

    private UPSProviderApi upsProviderApi;

    public UpsShippingRepository(UPSProviderApi upsProviderApi) {
        this.upsProviderApi = upsProviderApi;
    }

    public UpsShipmentApiResponse generateShipment(String transactionId, UpsShipmentRequest shipmentRequest) throws ShippingLabelException {
        return upsProviderApi.shipment(transactionId, shipmentRequest);
    }
}
