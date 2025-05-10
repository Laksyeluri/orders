package com.ecom.paymenthandler.model;

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
