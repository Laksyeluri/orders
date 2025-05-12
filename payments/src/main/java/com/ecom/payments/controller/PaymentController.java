package com.ecom.payments.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.payments.model.Payment;
import com.ecom.payments.repository.PaymentRepository;
import com.ecom.payments.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	PaymentRepository repository;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	PaymentService paymentService;
	
	@Autowired
	Environment environment;

	@PostMapping("")
	public ResponseEntity<Payment> makePayment(@RequestBody Payment payment) {
		log.info("Port: {}", environment.getProperty("local.server.port"));
		// Payment logic
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.makePayment(payment));
		} catch (Exception e) {
			log.info("Error while making payment", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public void deletePayment(@PathVariable Long id) {
		log.info("Port: {}", environment.getProperty("local.server.port"));
		paymentService.deleteByOrderId(id);
	}

	@GetMapping("/{paymentId}")
	public ResponseEntity<Payment> getPayment(@PathVariable Long paymentId) {
		log.info("Port: {}", environment.getProperty("local.server.port"));
		// Get payment logic
		try {
			return ResponseEntity.status(HttpStatus.FOUND).body(paymentService.getPayment(paymentId));
		} catch (Exception e) {
			// Log error
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<Payment> getPaymentByOrder(@PathVariable Long orderId) {
		log.info("Port: {}", environment.getProperty("local.server.port"));
		// Get payment by order logic
		try {
			return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
		} catch (Exception e) {
			// Log error
			return ResponseEntity.notFound().build();
		}
	}
}
