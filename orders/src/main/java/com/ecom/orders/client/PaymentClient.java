package com.ecom.orders.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecom.orders.model.Payment;

//@FeignClient(name = "payment-service", url = "http://localhost:8082")
@FeignClient(name = "payment-service")
public interface PaymentClient {

	@PostMapping("/payments")
	ResponseEntity<String> processPayment(@RequestBody Payment Payment);

	@DeleteMapping("/{id}")
	ResponseEntity<String> deletePayment(@PathVariable Long id);
}