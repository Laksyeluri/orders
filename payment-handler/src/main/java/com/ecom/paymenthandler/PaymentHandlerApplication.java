package com.ecom.paymenthandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentHandlerApplication.class, args);

	}

}
