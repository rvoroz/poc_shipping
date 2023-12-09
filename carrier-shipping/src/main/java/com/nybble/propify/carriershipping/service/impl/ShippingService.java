
package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.entities.CarrierShippingResponse;
import com.nybble.propify.carriershipping.entities.ShippingRequest;
import com.nybble.propify.carriershipping.model.Carrier;
import com.nybble.propify.carriershipping.model.CarrierShipment;
import com.nybble.propify.carriershipping.entities.Package;
import com.nybble.propify.carriershipping.entities.UpsShipmentRequest;
import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse;
import com.nybble.propify.carriershipping.exception.ServiceTypeNotFoundException;
import com.nybble.propify.carriershipping.exception.ShipmentParserException;
import com.nybble.propify.carriershipping.exception.ShippingBaseRunException;
import com.nybble.propify.carriershipping.mapper.repository.UpsShippingRepository;
import com.nybble.propify.carriershipping.model.Payment;
import com.nybble.propify.carriershipping.model.Shipper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ShippingService {
    private final ServiceTypeService serviceTypeService;
    private final UpsShippingRepository upsShippingRepository;
    private final PaymentService paymentService;
    private final ShipperService shipperService;
    private final CarrierService carrierService;
    private final CarrierShipmentService carrierShipmentService;

    @Value("${shipping.host}")
    private String API_HOST;

    public ShippingService(ServiceTypeService serviceTypeService, UpsShippingRepository upsShippingRepository, PaymentService paymentService, ShipperService shipperService, CarrierService carrierService, CarrierShipmentService carrierShipmentService){
        this.serviceTypeService = serviceTypeService;
        this.upsShippingRepository = upsShippingRepository;
        this.paymentService = paymentService;
        this.shipperService = shipperService;
        this.carrierService = carrierService;
        this.carrierShipmentService = carrierShipmentService;
    }

    public CarrierShippingResponse  processShipmentGeneration(String carrierLabel, ShippingRequest shippingRequest) throws ShippingBaseRunException {
        CarrierShippingResponse response = null;
            log.info("processLabelGeneration - validate Carrier - Request {} - {} carrier ", shippingRequest.getRequestId(), carrierLabel);
            Carrier carrier = carrierService.getValidCarrier(carrierLabel) ;
            log.info("processLabelGeneration - build carrier api request - Request {} - {} carrier ", shippingRequest.getRequestId(), carrier.getLabel());
            UpsShipmentRequest shipmentRequest = buildShipmentRequest(shippingRequest);
            log.info("processLabelGeneration - request shipment to carrier - Request {} - {} carrier ", shippingRequest.getRequestId(), carrier.getLabel());
            CarrierShipment carrierShipment = generateTruckingNumberAndShippingLabel(shippingRequest.getRequestId(), carrier, shipmentRequest);
            log.info("processLabelGeneration - persist carrier trackingNumber - Request {} - {} carrier ", shippingRequest.getRequestId(), carrier.getLabel());
            carrierShipmentService.createCarrierShipment(carrierShipment);
            log.info("processLabelGeneration - build response - Request {} - {} carrier ", shippingRequest.getRequestId(), carrier.getLabel());
            response = buildCarrierShippingResponse(carrier.getLabel(), shippingRequest.getRequestId() , carrierShipment.getTrackingNumber(), carrierShipment.getLabelId());
        return response;
    }

    private UpsShipmentRequest buildShipmentRequest(ShippingRequest shippingRequest) throws ShipmentParserException {
        if ( ! serviceTypeService.isServiceTypeAvailable(shippingRequest.getServiceType())) {
            throw new ServiceTypeNotFoundException(shippingRequest.getServiceType());
        }
        Payment payment = paymentService.getPaymentInformationByAccountNumber(shippingRequest.getShippingAccountId());
        Shipper shipper = shipperService.getShipperInfoByShipperNumber(shippingRequest.getShippingAccountId());
        Package aPackage = getDefaultPackage();
        return new UpsShipmentRequest(shipper, payment, shippingRequest.getServiceType(),
                shippingRequest.getShipFrom(), shippingRequest.getShipTo(), aPackage, "true");

    }

    private CarrierShipment generateTruckingNumberAndShippingLabel(UUID requestId, Carrier carrier, UpsShipmentRequest shipmentRequest) {
        String transactionId = requestId.toString().replace("-", "");
        UpsShipmentApiResponse shipment = upsShippingRepository.generateShipment(transactionId, shipmentRequest);
        return buildCarrierShipment(requestId, carrier, shipment);
    }

    private CarrierShipment buildCarrierShipment(UUID requestId, Carrier carrier, UpsShipmentApiResponse shipment) {
        try {
            String trackingNumber = shipment.getShipmentResponse().getShipmentResults().getPackageResults().getTrackingNumber();
            String providerLabelUrl = shipment.getShipmentResponse().getShipmentResults().getLabelURL();
            String shippingLabelImage = shipment.getShipmentResponse().getShipmentResults().getPackageResults().getShippingLabel().getGraphicImage();
            String labelId = UUID.randomUUID().toString();
            return CarrierShipment.builder().requestId(requestId.toString()).carrier(carrier.getId()).
                    labelId(labelId).providerLabelUrl(providerLabelUrl)
                    .trackingNumber(trackingNumber).shippingLabelImage(shippingLabelImage).build();
        } catch (Exception ex) {
            throw new ShipmentParserException(ex);
        }
    }


    private Package getDefaultPackage(){
        return new Package("02", "IN", "12.5",
                "9.5", "1" , "LBS", "0.5");
    }

    private CarrierShippingResponse buildCarrierShippingResponse(String carrierLabel, UUID requestId, String trackingNumber, String labelId ) {
        String labelUrl  = API_HOST + "/public/shipping/label/" + labelId;
        return CarrierShippingResponse.builder().carrier(carrierLabel).requestId(requestId)
                .labelUrl(labelUrl).trackingNumber(trackingNumber).build();
    }
}