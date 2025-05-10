package com.ecom.paymentexecutor.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.paymentexecutor.config.RabbitMQConfiguration;
import com.ecom.paymentexecutor.model.Payment;

@Service
public class GpayPaymentExecutorServiceImpl implements PaymentExecutorService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;

	@Override
	public Payment processPayment(Payment payment) {
		
		if(payment.getAmount() >1000) {
			throw new RuntimeException("Amount is >1000");
		}
		payment.setStatus("Success");
		rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, "processed_payment", payment);
		return payment;
	}

}
