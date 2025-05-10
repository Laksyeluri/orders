package com.ecom.paymentexecutor.controller;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ecom.paymentexecutor.config.RabbitMQConfiguration;
import com.ecom.paymentexecutor.model.Payment;
import com.ecom.paymentexecutor.service.PaymentExecutorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PaymentExecutorController {

	RabbitTemplate rabbitTemplate;

	PaymentExecutorController(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	PaymentExecutorService paymentExecutorService;

	@RabbitListener(queues = RabbitMQConfiguration.INPUT_QUEUE)
	public void processMessage(String message) {
		try {
			log.info("Received message from {} and message:{} ", RabbitMQConfiguration.INPUT_QUEUE, message);
			Payment payment = mapper.readValue(message, Payment.class);
			paymentExecutorService.processPayment(payment);
		} catch (Exception e) {
			log.error("Error while processing message", e);
			throw new AmqpRejectAndDontRequeueException("Send to DLQ");
		}
	}
}
