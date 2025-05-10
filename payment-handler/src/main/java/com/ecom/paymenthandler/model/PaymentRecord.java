package com.ecom.paymenthandler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentHandlerId;
	
	private Long id;
	private String orderId;
	private Double amount;
	private String status; // SUCCESS, FAILED, PENDING
}