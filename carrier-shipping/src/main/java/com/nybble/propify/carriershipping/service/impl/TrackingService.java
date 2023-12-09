
package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.entities.TrackingDetailResponse;
import com.nybble.propify.carriershipping.entities.UpsTrackingApiResponse;
import com.nybble.propify.carriershipping.exception.ShippingBaseRunException;
import com.nybble.propify.carriershipping.exception.TrackingParserException;
import com.nybble.propify.carriershipping.mapper.repository.UpsTrackingRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TrackingService {
    private final CarrierService carrierService;
    private final UpsTrackingRepository upsTrackingRepository;
    private static final String NO_VALUES = "N/A";

    public TrackingService(CarrierService carrierService, UpsTrackingRepository upsTrackingRepository) {
        this.carrierService = carrierService;
        this.upsTrackingRepository = upsTrackingRepository;
    }

    public TrackingDetailResponse findTrackPackageInformationByTrackingNumber(UUID requestId, String carrierLabel, String trackingNumber)
            throws ShippingBaseRunException {
            log.info("findTrackPackageInformationByTrackingNumber - validate Carrier - trackingNumber '{}' - {} carrier ", trackingNumber, carrierLabel);
            carrierService.getValidCarrier(carrierLabel) ;
            log.info("findTrackPackageInformationByTrackingNumber - validate Carrier - trackingNumber '{}' - {} carrier ", trackingNumber, carrierLabel);
            UpsTrackingApiResponse trackDetails = getTrackDetails(requestId, trackingNumber);
            log.info("findTrackPackageInformationByTrackingNumber - build response - trackingNumber '{}' - {} carrier ", trackingNumber, carrierLabel);
            return buildTrackingDetail(requestId,carrierLabel, trackingNumber, trackDetails);
    }

    private UpsTrackingApiResponse getTrackDetails(UUID requestId, String trackingNumber) {
        return upsTrackingRepository.getTrackDetail(requestId.toString(), trackingNumber);
    }

    private TrackingDetailResponse buildTrackingDetail (UUID requestId, String carrier, String trackingNumber,
                                                        UpsTrackingApiResponse upsTrackingApiResponse) throws TrackingParserException {
        TrackingDetailResponse response = null;
        try {
            response = TrackingDetailResponse.builder()
                    .shipments(new ArrayList<>()).requestId(requestId).carrier(carrier.toUpperCase()).requestTrackingNumber(trackingNumber).build();
            for (UpsTrackingApiResponse.Shipment shipment : upsTrackingApiResponse.getTrackResponse().getShipment()) {
                response.getShipments().addAll(buildTrackingShipment(shipment));
            }
        } catch (Exception ex) {
            throw new TrackingParserException(ex);
        }
        return response;
    }

    private List<TrackingDetailResponse.Shipment> buildTrackingShipment(UpsTrackingApiResponse.Shipment tdShipment) {
        List<TrackingDetailResponse.Shipment> resShipments = new ArrayList<>();
        tdShipment.getAPackage().forEach(aPackage -> {
            String trackingNumber = aPackage.getTrackingNumber();
            String deliveryDate = (aPackage.getDeliveryDate().isEmpty()) ? NO_VALUES : aPackage.getDeliveryDate().get(0).getDate();
            String deliveryTime = (aPackage.getDeliveryTime().isPresent()) ? aPackage.getDeliveryTime().get().getEndTime() : NO_VALUES;
            Optional<UpsTrackingApiResponse.Status> status =
                    (aPackage.getActivity().isEmpty()) ? Optional.empty() : Optional.of(aPackage.getActivity().get(0).getStatus());
            String statusDescription = (status.isPresent()) ? status.get().getDescription().trim() : NO_VALUES;
            String statusCode = (status.isPresent()) ? status.get().getCode() : NO_VALUES;
            String statusCodeStatus = (status.isPresent()) ? status.get().getStatusCode() : NO_VALUES;
            resShipments.add(
                    TrackingDetailResponse.Shipment.builder()
                            .trackingNumber(trackingNumber)
                            .deliveryDate(deliveryDate)
                            .deliveryTime(deliveryTime)
                            .statusDescription(statusDescription)
                            .statusCode(statusCode)
                            .statusCodeStatus(statusCodeStatus)
                            .build());
        });

        return resShipments;
    }
}