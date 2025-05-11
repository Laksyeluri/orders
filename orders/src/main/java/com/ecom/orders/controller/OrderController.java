package com.ecom.orders.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.orders.model.Order;
import com.ecom.orders.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	Environment environment;

	@PostMapping
	public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
		Order createdOrder = orderService.placeOrder(order);
		log.info("Port: {}", environment.getProperty("local.server.port"));
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		return orderService.getOrderById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping
	public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
		log.info("updated order:{}", order);
		Order createdOrder = orderService.updateOrder(order);
		return ResponseEntity.ok(createdOrder);
	}

	@DeleteMapping("/{id}")
	public void deleteOrder(@PathVariable Long id) {
		log.info("Deleting order:{}", id);
		orderService.deleteOrder(id);
	}

	@GetMapping("/failed")
	public ResponseEntity<List<Order>> getFailedOrders() {
		List<Order> failedOrders = orderService.getFailedOrders();
		return ResponseEntity.ok(failedOrders);
	}

	@GetMapping("/success")
	public ResponseEntity<List<Order>> getSuccessOrders() {
		List<Order> successOrders = orderService.getSuccessOrders();
		return ResponseEntity.ok(successOrders);
	}

}