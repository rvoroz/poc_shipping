package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.exception.PaymentNotFoundException;
import com.nybble.propify.carriershipping.mapper.PaymentInformationMapper;
import com.nybble.propify.carriershipping.model.Payment;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentInformationMapper paymentInformationMapper;

    public PaymentService(PaymentInformationMapper paymentInformationMapper) {
        this.paymentInformationMapper = paymentInformationMapper;
    }

    public Payment getPaymentInformationByAccountNumber(String accountNumber) throws PaymentNotFoundException {
        Optional<Payment> payment = paymentInformationMapper.getPaymentInfoByAccountNumber(accountNumber);
        if (payment.isPresent()) {
            return payment.get();
        } else {
            throw new PaymentNotFoundException(accountNumber);
        }
    }

}
