package com.nybble.propify.carriershipping.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nybble.propify.carriershipping.entities.CandidateAddress;
import com.nybble.propify.carriershipping.entities.ShipRequest;
import com.nybble.propify.carriershipping.entities.ShippingRequest;
import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse;
import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse.PackageResults;
import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse.ShipmentResponse;
import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse.ShipmentResults;
import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse.ShippingLabel;
import com.nybble.propify.carriershipping.entities.UpsShipmentRequest;
import com.nybble.propify.carriershipping.exception.CarrierNotFoundException;
import com.nybble.propify.carriershipping.exception.ServiceTypeNotFoundException;
import com.nybble.propify.carriershipping.exception.ShipmentParserException;
import com.nybble.propify.carriershipping.mapper.repository.UpsShippingRepository;
import com.nybble.propify.carriershipping.model.Carrier;
import com.nybble.propify.carriershipping.model.CarrierShipment;
import com.nybble.propify.carriershipping.model.Payment;
import com.nybble.propify.carriershipping.model.Shipper;
import com.nybble.propify.carriershipping.service.impl.CarrierService;
import com.nybble.propify.carriershipping.service.impl.CarrierShipmentService;
import com.nybble.propify.carriershipping.service.impl.PaymentService;
import com.nybble.propify.carriershipping.service.impl.ServiceTypeService;
import com.nybble.propify.carriershipping.service.impl.ShipperService;
import com.nybble.propify.carriershipping.service.impl.ShippingService;

@RunWith(SpringRunner.class)
public class ShippingServiceTest {

    private ShippingService shippingService;

    @MockBean
    private ServiceTypeService serviceTypeService;
    @MockBean
    private UpsShippingRepository upsShippingRepository;
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private ShipperService shipperService;
    @MockBean
    private CarrierService carrierService;
    @MockBean
    private CarrierShipmentService carrierShipmentService;

    @Before
    public void setUp() {
        shippingService = new ShippingService(serviceTypeService, upsShippingRepository, paymentService, shipperService,
                carrierService, carrierShipmentService);
    }

    @Test
    public void processShipmentGeneration_whenCarrierLabelAndShippingRequestAreOk_returnCarrierShippingResponse() {
        Carrier carrier = Carrier.builder().label("label").build();
        when(carrierService.getValidCarrier(anyString())).thenReturn(carrier);

        when(serviceTypeService.isServiceTypeAvailable(anyString())).thenReturn(true);
        Payment payment = new Payment();
        when(paymentService.getPaymentInformationByAccountNumber(anyString())).thenReturn(payment);
        Shipper shipper = new Shipper();
        when(shipperService.getShipperInfoByShipperNumber(anyString())).thenReturn(shipper);

        CandidateAddress candidateAddress = CandidateAddress.builder().build();
        ShipRequest shipFrom = new ShipRequest("shipFrom", candidateAddress);
        ShipRequest shipTo = new ShipRequest("shipTo", candidateAddress);
        ShippingRequest request = new ShippingRequest(UUID.randomUUID(), shipFrom, shipTo, "1", "A");

        UpsShipmentApiResponse upsShipmentApiResponse = new UpsShipmentApiResponse();
        ShipmentResponse shipmentResponse = new ShipmentResponse();
        ShipmentResults shipmentResults = new ShipmentResults();
        shipmentResults.setShipmentIdentificationNumber("11111");
        shipmentResults.setLabelURL("labelURL");
        PackageResults packageResults = new PackageResults();
        packageResults.setTrackingNumber("123134");
        ShippingLabel shippingLabel = new ShippingLabel();
        shippingLabel.setGraphicImage("graphic image");
        packageResults.setShippingLabel(shippingLabel);
        shipmentResults.setPackageResults(packageResults);
        shipmentResponse.setShipmentResults(shipmentResults);
        upsShipmentApiResponse.setShipmentResponse(shipmentResponse);
        when(upsShippingRepository.generateShipment(anyString(), any(UpsShipmentRequest.class)))
                .thenReturn(upsShipmentApiResponse);

        doNothing().when(carrierShipmentService).createCarrierShipment(any(CarrierShipment.class));

        shippingService.processShipmentGeneration("aaa", request);

        verify(carrierService, times(1)).getValidCarrier(anyString());
        verify(serviceTypeService, timeout(1)).isServiceTypeAvailable(anyString());
        verify(paymentService, times(1)).getPaymentInformationByAccountNumber(anyString());
        verify(shipperService, times(1)).getShipperInfoByShipperNumber(anyString());
        verify(upsShippingRepository, times(1)).generateShipment(anyString(), any(UpsShipmentRequest.class));
        verify(carrierShipmentService, times(1)).createCarrierShipment(any(CarrierShipment.class));

    }

    @Test(expected = CarrierNotFoundException.class)
    public void processShipmentGeneration_whenCarrierNotFound_returnException() {
        when(carrierService.getValidCarrier(anyString())).thenThrow(new CarrierNotFoundException("carrier not found"));

        CandidateAddress candidateAddress = CandidateAddress.builder().build();
        ShipRequest shipFrom = new ShipRequest("shipFrom", candidateAddress);
        ShipRequest shipTo = new ShipRequest("shipTo", candidateAddress);
        ShippingRequest request = new ShippingRequest(UUID.randomUUID(), shipFrom, shipTo, "1", "A");

        UpsShipmentApiResponse upsShipmentApiResponse = new UpsShipmentApiResponse();
        ShipmentResponse shipmentResponse = new ShipmentResponse();
        ShipmentResults shipmentResults = new ShipmentResults();
        shipmentResults.setShipmentIdentificationNumber("11111");
        shipmentResults.setLabelURL("labelURL");
        PackageResults packageResults = new PackageResults();
        packageResults.setTrackingNumber("123134");
        ShippingLabel shippingLabel = new ShippingLabel();
        shippingLabel.setGraphicImage("graphic image");
        packageResults.setShippingLabel(shippingLabel);
        shipmentResults.setPackageResults(packageResults);
        shipmentResponse.setShipmentResults(shipmentResults);
        upsShipmentApiResponse.setShipmentResponse(shipmentResponse);
        
        shippingService.processShipmentGeneration("aaa", request);

        verify(carrierService, times(1)).getValidCarrier(anyString());
        verify(serviceTypeService, timeout(0)).isServiceTypeAvailable(anyString());
        verify(paymentService, times(0)).getPaymentInformationByAccountNumber(anyString());
        verify(shipperService, times(0)).getShipperInfoByShipperNumber(anyString());
        verify(upsShippingRepository, times(0)).generateShipment(anyString(), any(UpsShipmentRequest.class));
        verify(carrierShipmentService, times(0)).createCarrierShipment(any(CarrierShipment.class));

    }

    @Test(expected = ServiceTypeNotFoundException.class)
    public void processShipmentGeneration_whenServiceTypeNotFound_returnServiceTypeNotFoundException() {
        Carrier carrier = Carrier.builder().label("label").build();
        when(carrierService.getValidCarrier(anyString())).thenReturn(carrier);

        when(serviceTypeService.isServiceTypeAvailable(anyString())).thenReturn(false);

        CandidateAddress candidateAddress = CandidateAddress.builder().build();
        ShipRequest shipFrom = new ShipRequest("shipFrom", candidateAddress);
        ShipRequest shipTo = new ShipRequest("shipTo", candidateAddress);
        ShippingRequest request = new ShippingRequest(UUID.randomUUID(), shipFrom, shipTo, "1", "A");

        UpsShipmentApiResponse upsShipmentApiResponse = new UpsShipmentApiResponse();
        ShipmentResponse shipmentResponse = new ShipmentResponse();
        ShipmentResults shipmentResults = new ShipmentResults();
        shipmentResults.setShipmentIdentificationNumber("11111");
        shipmentResults.setLabelURL("labelURL");
        PackageResults packageResults = new PackageResults();
        packageResults.setTrackingNumber("123134");
        ShippingLabel shippingLabel = new ShippingLabel();
        shippingLabel.setGraphicImage("graphic image");
        packageResults.setShippingLabel(shippingLabel);
        shipmentResults.setPackageResults(packageResults);
        shipmentResponse.setShipmentResults(shipmentResults);
        upsShipmentApiResponse.setShipmentResponse(shipmentResponse);

        shippingService.processShipmentGeneration("aaa", request);

        verify(carrierService, times(1)).getValidCarrier(anyString());
        verify(serviceTypeService, timeout(1)).isServiceTypeAvailable(anyString());
        verify(paymentService, times(0)).getPaymentInformationByAccountNumber(anyString());
        verify(shipperService, times(0)).getShipperInfoByShipperNumber(anyString());
        verify(upsShippingRepository, times(0)).generateShipment(anyString(), any(UpsShipmentRequest.class));
        verify(carrierShipmentService, times(0)).createCarrierShipment(any(CarrierShipment.class));

    }

    @Test(expected = ShipmentParserException.class)
    public void processShipmentGeneration_whenShipmentParserNotFound_returnShipmentParserException() {
        Carrier carrier = Carrier.builder().label("label").build();
        when(carrierService.getValidCarrier(anyString())).thenReturn(carrier);

        when(serviceTypeService.isServiceTypeAvailable(anyString())).thenReturn(true);

        CandidateAddress candidateAddress = CandidateAddress.builder().build();
        ShipRequest shipFrom = new ShipRequest("shipFrom", candidateAddress);
        ShipRequest shipTo = new ShipRequest("shipTo", candidateAddress);
        ShippingRequest request = new ShippingRequest(UUID.randomUUID(), shipFrom, shipTo, "1", "A");

        UpsShipmentApiResponse upsShipmentApiResponse = new UpsShipmentApiResponse();
        ShipmentResponse shipmentResponse = new ShipmentResponse();
        ShipmentResults shipmentResults = new ShipmentResults();
        shipmentResults.setShipmentIdentificationNumber("11111");
        shipmentResults.setLabelURL("labelURL");
        PackageResults packageResults = new PackageResults();
        packageResults.setTrackingNumber("123134");
        ShippingLabel shippingLabel = new ShippingLabel();
        shippingLabel.setGraphicImage("graphic image");
        packageResults.setShippingLabel(shippingLabel);
        shipmentResults.setPackageResults(packageResults);
        shipmentResponse.setShipmentResults(shipmentResults);
        upsShipmentApiResponse.setShipmentResponse(shipmentResponse);

        Payment payment = new Payment();
        when(paymentService.getPaymentInformationByAccountNumber(anyString())).thenReturn(payment);

        Shipper shipper = new Shipper();
        when(shipperService.getShipperInfoByShipperNumber(anyString())).thenReturn(shipper);

        when(upsShippingRepository.generateShipment(anyString(), any(UpsShipmentRequest.class)))
                .thenThrow(new ShipmentParserException(new Throwable("Error")));

        shippingService.processShipmentGeneration("aaa", request);

        verify(carrierService, times(1)).getValidCarrier(anyString());
        verify(serviceTypeService, timeout(1)).isServiceTypeAvailable(anyString());
        verify(paymentService, times(0)).getPaymentInformationByAccountNumber(anyString());
        verify(shipperService, times(0)).getShipperInfoByShipperNumber(anyString());
        verify(upsShippingRepository, times(1)).generateShipment(anyString(), any(UpsShipmentRequest.class));
        verify(carrierShipmentService, times(0)).createCarrierShipment(any(CarrierShipment.class));

    }
}
