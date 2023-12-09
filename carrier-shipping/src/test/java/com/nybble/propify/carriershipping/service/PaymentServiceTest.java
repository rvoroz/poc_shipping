package com.nybble.propify.carriershipping.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nybble.propify.carriershipping.exception.PaymentNotFoundException;
import com.nybble.propify.carriershipping.mapper.CarrierShipmentMapper;
import com.nybble.propify.carriershipping.mapper.PaymentInformationMapper;
import com.nybble.propify.carriershipping.model.CarrierShipment;
import com.nybble.propify.carriershipping.model.Payment;
import com.nybble.propify.carriershipping.service.impl.CarrierShipmentService;
import com.nybble.propify.carriershipping.service.impl.PaymentService;

@RunWith(SpringRunner.class)
public class PaymentServiceTest {

    private PaymentService paymentService;

    @MockBean
    private PaymentInformationMapper paymentInformationMapper;

    @Before
    public void setUp() {
        paymentService = new PaymentService(paymentInformationMapper);
    }

    @Test
    public void getPaymentInformationByAccountNumber_whenAccountOk_returnPayment() {
        Payment payment = new Payment();
        Optional<Payment> optionalPayment = Optional.of(payment);
        when(paymentInformationMapper.getPaymentInfoByAccountNumber(anyString())).thenReturn(optionalPayment);

        paymentService.getPaymentInformationByAccountNumber("paymentAccount");

        verify(paymentInformationMapper, times(1)).getPaymentInfoByAccountNumber(anyString());

    }

    @Test(expected = PaymentNotFoundException.class)
    public void getPaymentInformationByAccountNumber_whenAccountNotFound_returnException() {
        Optional<Payment> optionalPayment = Optional.empty();
        when(paymentInformationMapper.getPaymentInfoByAccountNumber(anyString())).thenReturn(optionalPayment);

        paymentService.getPaymentInformationByAccountNumber("paymentAccount");

        verify(paymentInformationMapper, times(1)).getPaymentInfoByAccountNumber(anyString());

    }

}
