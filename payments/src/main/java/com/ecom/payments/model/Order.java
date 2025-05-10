package com.ecom.payments.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Order  {

	private Long id;

	private String customerName;

	private Double amount;

	private String status;

}