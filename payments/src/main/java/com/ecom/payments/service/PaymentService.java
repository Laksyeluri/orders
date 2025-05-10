package com.ecom.payments.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.payments.model.Payment;
import com.ecom.payments.repository.PaymentRepository;
import com.ecom.payments.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {

	@Autowired
	PaymentRepository repository;

	@Autowired
	RabbitTemplate rabbitTemplate;

	public Payment makePayment(Payment payment) {
		// Payment logic
		try {
//            docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management
			ObjectMapper mapper = new ObjectMapper();
			payment.setStatus("ACCEPTED");
			repository.save(payment);
			String paymentJson = mapper.writeValueAsString(payment);
			sendRabbitMQMessage(Constants.PAYMENT_EXCHANGE_NAME, Constants.PAYMENT_ROUTING_KEY, paymentJson);
			return payment;
		} catch (Exception e) {
			// Log error
			log.error("Error while making payment", e);
			e.printStackTrace();
		}
		return payment;
	}

	private void sendRabbitMQMessage(String exchange, String routingKey, String paymentJson) {
		log.info("Sending to rabbitMQ with routing key {}", routingKey);
		rabbitTemplate.convertAndSend(exchange, routingKey, paymentJson);
	}

	public void deleteByOrderId(Long id) {
		repository.deleteByOrderId(id);
	}

	public Payment getPayment(Long paymentId) {
		// Get payment logic
		Payment payment = null;
		try {
			payment = repository.findById(paymentId).orElseThrow();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payment;
	}

	public Payment getPaymentByOrderId(Long orderId) {
		// Get payment by order logic
		Payment payment = null;
		try {
			payment = repository.findByOrderId(orderId).orElseThrow();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payment;
	}

}
