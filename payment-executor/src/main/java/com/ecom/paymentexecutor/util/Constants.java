package com.ecom.paymentexecutor.util;

public class Constants {

	public static final String PAYMENT_EXCHANGE = "payment_exchange";
	public static final String PAYMENT_QUEUE = "payment_queue";
	public static final String PAYMENT_ROUTING_KEY = "payment";

	public static final String PROCESSED_PAYMENTS_EXCHANGE = "processed_payments_exchange";
	public static final String PROCESSED_PAYMENTS_QUEUE = "processed_payments_queue";
	public static final String PROCESSED_PAYMENTS_ROUTING_KEY = "processed_payment";

	public static final String DLX_NAME = PAYMENT_EXCHANGE + ".dlx";
	public static final String DLQ_NAME = PAYMENT_QUEUE + ".dlq";
	public static final String DLX_ROUTING_KEY = PAYMENT_ROUTING_KEY + ".failures";

}
