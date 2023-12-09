package com.nybble.propify.carriershipping.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
import com.nybble.propify.carriershipping.mapper.CarrierMapper;
import com.nybble.propify.carriershipping.model.Carrier;
import com.nybble.propify.carriershipping.model.CarrierShipment;
import com.nybble.propify.carriershipping.model.Payment;
import com.nybble.propify.carriershipping.model.Shipper;
import com.nybble.propify.carriershipping.service.impl.CarrierService;

@RunWith(SpringRunner.class)
public class CarrierServiceTest {

    private CarrierService carrierService;

    @MockBean
    private CarrierMapper carrierMapper;

    @Before
    public void setUp() {
        carrierService = new CarrierService(carrierMapper);
    }

    @Test
    public void getValidCarrier_whenCarrierIsOk_returnCarrier() {
        Carrier carrier = Carrier.builder().build();
        Optional<Carrier> optionalCarrier = Optional.of(carrier);
        when(carrierMapper.findCarrierByLabel(anyString())).thenReturn(optionalCarrier);

        carrierService.getValidCarrier("carrierLabel");

        verify(carrierMapper, times(1)).findCarrierByLabel(anyString());

    }

    @Test(expected = CarrierNotFoundException.class)
    public void getValidCarrier_whenCarrierNotFound_returnException() {
        Optional<Carrier> optionalCarrier = Optional.empty();
        when(carrierMapper.findCarrierByLabel(anyString())).thenReturn(optionalCarrier);

        carrierService.getValidCarrier("carrierLabel");

        verify(carrierMapper, times(1)).findCarrierByLabel(anyString());

    }

}
