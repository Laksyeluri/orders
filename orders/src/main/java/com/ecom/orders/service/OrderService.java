package com.ecom.orders.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.orders.client.PaymentClient;
import com.ecom.orders.model.Order;
import com.ecom.orders.model.OrderItem;
import com.ecom.orders.model.Payment;
import com.ecom.orders.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	PaymentClient paymentClient;

	public Order placeOrder(Order order) {
		order.setStatus("ACCEPTED");

		for (OrderItem item : order.getItems()) {
			item.setOrder(order); // Ensure proper mapping
		}
//        return orderRepository.save(order);

		Order savedOrder = orderRepository.save(order);

		// send it to payments service
		Payment payment = new Payment(null, order.getId(), order.getAmount(), order.getStatus());
		paymentClient.processPayment(payment);
		return savedOrder;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<Order> getOrderById(Long id) {
		return getAllOrders().stream().filter(order -> order.getId().equals(id)).findFirst();
	}

	public Order updateOrder(Order order) {
		Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
		updatedOrder.setStatus(order.getStatus());
		orderRepository.save(updatedOrder);
		return updatedOrder;
	}

	public void deleteOrder(Long id) {
		Optional<Order> foundOrder = orderRepository.findById(id);
		if (foundOrder.isPresent()) {
			Order order = foundOrder.get();
			Payment payment = new Payment(null, order.getId(), order.getAmount(), order.getStatus());
			orderRepository.delete(order);
			paymentClient.deletePayment(payment.getOrderId());
		}
	}

	public List<Order> getFailedOrders() {
		return orderRepository.findByStatus("FAILED"); // Filter failed orders
	}

	public List<Order> getSuccessOrders() {
		return orderRepository.findByStatus("SUCCESS"); // Filter Success orders
	}
}