package com.ecom.payments.service;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.payments.client.OrderClient;
import com.ecom.payments.model.Order;
import com.ecom.payments.model.Payment;
import com.ecom.payments.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentSuccessService {
	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	OrderClient orderClient;

	@Autowired
	ObjectMapper mapper;

	public static final String PAYMENT_EXECUTOR_QUEUE = "processed_payments_queue";

	@RabbitListener(queues = PAYMENT_EXECUTOR_QUEUE)
	public void processMessage(String message) {
		Payment payment = new Payment();
		try {
			log.info("Received message from executor: {}", message);
			String cleanedJson = message.replaceAll("^\"|\"$", "") // Removes extra quotes at start and end
					.replace("\\", ""); // Fixes escaping issues

			log.info("Json Payload:{}", cleanedJson);
			payment = mapper.readValue(cleanedJson, Payment.class);
			Optional<Payment> existedPayment = paymentRepository.findById(payment.getId());
			if (existedPayment.isPresent()) {
				Payment payment1 = existedPayment.get();
				payment1.setStatus("SUCCESS");
				paymentRepository.save(payment1);

				// update Orders
				Order order = new Order(payment1.getOrderId(), null, payment1.getAmount(), payment1.getStatus());
				// call orders client to update status

				orderClient.processOrder(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}