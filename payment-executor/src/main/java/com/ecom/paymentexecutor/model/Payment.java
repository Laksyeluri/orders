package com.ecom.paymentexecutor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	private Long id;
	private Long orderId;
	private double amount;
	private String status;

}
