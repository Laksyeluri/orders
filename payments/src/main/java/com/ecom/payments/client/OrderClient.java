package com.ecom.payments.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecom.payments.model.Order;

@FeignClient(name = "order-service", url = "http://localhost:8081")
public interface OrderClient {

	@PutMapping("/orders")
	ResponseEntity<String> processOrder(@RequestBody Order order);

}
