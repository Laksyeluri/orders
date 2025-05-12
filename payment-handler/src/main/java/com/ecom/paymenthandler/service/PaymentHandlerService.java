package com.ecom.paymenthandler.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ecom.paymenthandler.config.RabbitMQConfiguration;
import com.ecom.paymenthandler.model.Payment;
import com.ecom.paymenthandler.model.PaymentRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentHandlerService {

	private static final String PAYMENT_QUEUE_DLQ = "payment_queue.dlq";

	public static final String PAYMENT_EXECUTOR_QUEUE = "processed_payments_queue";

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	Environment environment;

//	@Autowired
//	PaymentService paymentService;

	@Value("${spring.rabbitmq.retry.interval.inMillis}")
	private int retryInMillis;

	@Autowired
	private PaymentService paymentService;

	@RabbitListener(queues = PAYMENT_EXECUTOR_QUEUE)
	public void processMessage(String message) {
		log.info("Port: {}", environment.getProperty("local.server.port"));
//		Payment payment = new Payment();
		try {
			log.info("Received message from executor: {}", message);
			String cleanedJson = message.replaceAll("^\"|\"$", "") // Removes extra quotes at start and end
					.replace("\\", ""); // Fixes escaping issues

			log.info("Json Payload:{}", cleanedJson);
//			payment = mapper.readValue(cleanedJson, Payment.class);

			rabbitTemplate.convertAndSend(RabbitMQConfiguration.PAYMENT_EXCHANGE,
					RabbitMQConfiguration.PAYMENT_SUCCESS_ROUTING_KEY, cleanedJson);

			saveToDB(cleanedJson, "SUCCESS");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RabbitListener(queues = PAYMENT_QUEUE_DLQ)
	public void processFailedPayment(Message message, String payload) {
		log.info("Port: {}", environment.getProperty("local.server.port"));
		log.info("{} Message properties:{}", PAYMENT_QUEUE_DLQ, message.getMessageProperties());
		String orderId = new String(message.getBody());

		String cleanedJson = orderId.replaceAll("^\"|\"$", "") // Removes extra quotes at start and end
				.replace("\\", ""); // Fixes escaping issues

		log.info("Json Payload:{}", cleanedJson);

		int retryCount = message.getMessageProperties().getHeader("x-retry-count") == null ? 0
				: (int) message.getMessageProperties().getHeader("x-retry-count");

		if (retryCount < 3) {
			int nextDelay = (int) (retryInMillis * Math.pow(2, retryCount)); // Exponential backoff (5s, 10s, 20s)

			rabbitTemplate.convertAndSend(RabbitMQConfiguration.PAYMENT_RETRY_EXCHANGE,
					RabbitMQConfiguration.PAYMENT_RETRY_ROUTING_KEY, cleanedJson, msg -> {
						msg.getMessageProperties().setHeader("x-retry-count", retryCount + 1);
						msg.getMessageProperties().setExpiration(String.valueOf(nextDelay)); // Set TTL dynamically
						return msg;
					});

			log.info("Retrying message with exponential backoff: {} ms", nextDelay);

		} else {
			log.info("Failed after max retries");
			Payment payment = null;
			try {

				payment = mapper.readValue(cleanedJson, Payment.class);
				payment.setStatus("FAILED");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			rabbitTemplate.convertAndSend(RabbitMQConfiguration.PAYMENT_EXCHANGE,
					RabbitMQConfiguration.PAYMENT_FAILURE_ROUTING_KEY, payment);
//
//			// Optional: Send alert
			sendFailureAlert(orderId);
			saveToDB(cleanedJson, "FAILED");
		}
	}

	private void saveToDB(String cleanedJson, String status) {
		PaymentRecord paymentRecord = null;
		try {
			paymentRecord = mapper.readValue(cleanedJson, PaymentRecord.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		paymentRecord.setStatus(status);
		paymentService.saveToDatabase(paymentRecord);
	}

	private void sendFailureAlert(String orderId) {
		log.info("Sending Email Alert");
		log.error("Email setup not ready");

	}

}
