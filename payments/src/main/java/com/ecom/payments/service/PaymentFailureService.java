package com.ecom.payments.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.payments.client.OrderClient;
import com.ecom.payments.model.Order;
import com.ecom.payments.model.Payment;
import com.ecom.payments.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentFailureService {

	public static final String PAYMENT_FAILURE_QUEUE = "payment_failure_queue";

	private static final String FAILED = "FAILED";


	@Autowired
	ObjectMapper mapper;

	@Autowired
	PaymentRepository paymentRepository;

	
	@Autowired
	OrderClient orderClient;

	@RabbitListener(queues = PAYMENT_FAILURE_QUEUE)
	public void processMessage(String message) {

		try {
			Payment payment = mapper.readValue(message, Payment.class);
			payment.setStatus(FAILED);

			Payment updatedPayment = paymentRepository.save(payment);
			
			Order order = new Order(updatedPayment.getOrderId(), null, updatedPayment.getAmount(), updatedPayment.getStatus());
			
			// call orders client to update status
			orderClient.processOrder(order);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
