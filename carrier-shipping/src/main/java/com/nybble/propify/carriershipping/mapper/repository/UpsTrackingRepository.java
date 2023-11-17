package com.nybble.propify.carriershipping.mapper.repository;

import com.nybble.propify.carriershipping.entities.UpsTrackingApiResponse;
import com.nybble.propify.carriershipping.exception.TrackingException;
import com.nybble.propify.carriershipping.provider.UPSProviderApi;

import org.springframework.stereotype.Service;


@Service
public class UpsTrackingRepository {

    private UPSProviderApi upsProviderApi;

    public UpsTrackingRepository(UPSProviderApi upsProviderApi) {
        this.upsProviderApi = upsProviderApi;
    }

    public UpsTrackingApiResponse getTrackDetail(String transactionId, String trackingNumber) throws TrackingException {
        return upsProviderApi.tracking(transactionId, trackingNumber);
    }
}
