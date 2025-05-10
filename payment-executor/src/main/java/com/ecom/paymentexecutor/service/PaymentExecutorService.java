package com.ecom.paymentexecutor.service;

import org.springframework.stereotype.Service;

import com.ecom.paymentexecutor.model.Payment;

@Service
public interface PaymentExecutorService {

	Payment processPayment(Payment payment);

}
