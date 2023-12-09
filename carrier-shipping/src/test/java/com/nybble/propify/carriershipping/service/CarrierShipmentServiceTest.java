package com.nybble.propify.carriershipping.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nybble.propify.carriershipping.exception.InternalErrorException;
import com.nybble.propify.carriershipping.mapper.CarrierShipmentMapper;
import com.nybble.propify.carriershipping.model.CarrierShipment;
import com.nybble.propify.carriershipping.service.impl.CarrierShipmentService;

@RunWith(SpringRunner.class)
public class CarrierShipmentServiceTest {

    private CarrierShipmentService carrierShipmentService;

    @MockBean
    private CarrierShipmentMapper carrierShipmentMapper;

    @Before
    public void setUp() {
        carrierShipmentService = new CarrierShipmentService(carrierShipmentMapper);
    }

    @Test
    public void createCarrierShipment_whenCarrierShipmentIsOk_persistCarrierShipment() {
        CarrierShipment carrierShipment = CarrierShipment.builder().build();
        doNothing().when(carrierShipmentMapper).insert(any(CarrierShipment.class));

        carrierShipmentService.createCarrierShipment(carrierShipment);

        verify(carrierShipmentMapper, times(1)).insert(any(CarrierShipment.class));

    }

}
