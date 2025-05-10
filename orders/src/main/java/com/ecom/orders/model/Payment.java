package com.ecom.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Payment {

	private Long id;
	private Long orderId;
	private double amount;
	private String status;
}