package com.ecom.paymentexecutor.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.paymentexecutor.config.RabbitMQConfiguration;
import com.ecom.paymentexecutor.model.Payment;
import com.ecom.paymentexecutor.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GpayPaymentExecutorServiceImpl implements PaymentExecutorService {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	ObjectMapper mapper;

	@Override
	public Payment processPayment(Payment payment) {

		if (payment.getAmount() > 1000) {
			throw new RuntimeException("Amount is >1000");
		}
		payment.setStatus("Success");
		String paymentJson = "";
		try {
			paymentJson = mapper.writeValueAsString(payment);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		sendRabbitMQMessage(Constants.PROCESSED_PAYMENTS_EXCHANGE, Constants.PROCESSED_PAYMENTS_ROUTING_KEY,
				paymentJson);
		return payment;
	}

	private void sendRabbitMQMessage(String exchange, String routingKey, String paymentJson) {
		log.info("Sending to rabbitMQ with routing key {}", routingKey);
		rabbitTemplate.convertAndSend(exchange, routingKey, paymentJson);
	}

}
