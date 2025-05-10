package com.ecom.payments.util;

public class Constants {

	public static final String FAILED = "FAILED";
	public static final String SUCCESS = "SUCCESS";
	public static final String ORDER_SERVICE = "order-service";

	public static final String PAYMENT_FAILURE_QUEUE = "payment_failure_queue";
	public static final String PAYMENT_SUCCESS_QUEUE = "payment_success_queue";

	public static final String PAYMENT_EXCHANGE_NAME = "payment_exchange";
	public static final String PAYMENT_QUEUE_NAME = "payment_queue";
	public static final String PAYMENT_ROUTING_KEY = "payment";

	public static final String DLQ_NAME = PAYMENT_QUEUE_NAME + ".dlq";
	public static final String DLX_EXCHANGE_NAME = PAYMENT_EXCHANGE_NAME + ".dlx";
	public static final String DLX_ROUTING_KEY = PAYMENT_ROUTING_KEY + ".failures";

}
