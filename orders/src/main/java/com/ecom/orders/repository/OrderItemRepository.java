package com.ecom.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.orders.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
