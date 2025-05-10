package com.ecom.paymentexecutor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecom.paymentexecutor.util.Constants;

@Configuration
public class RabbitMQConfiguration {

//	public static final String INPUT_EXCHANGE = "payment_exchange";
//	public static final String INPUT_QUEUE = "payment_queue";
//	public static final String PAYMENTROUTING_KEY = "payment";
//
//	public static final String EXCHANGE_NAME = "processed_payments_exchange";
//	public static final String QUEUE_NAME = "processed_payments_queue";
//
//	public static final String DLX_NAME = INPUT_EXCHANGE + ".dlx";
//
//	public static final String DLQ_NAME = INPUT_QUEUE + ".dlq";
//
//	public static final String DLX_ROUTING_KEY = PAYMENTROUTING_KEY + ".failures";

	// MainQueue with DL configs
	@Bean
	Queue paymentQueue() {
		return QueueBuilder.durable(Constants.PAYMENT_QUEUE).withArgument("x-dead-letter-exchange", Constants.DLX_NAME)
				.withArgument("x-dead-letter-routing-key", Constants.DLQ_NAME).build();
	}

	// processed-payments configs
	@Bean
	Queue processedPaymentsQueue() {
		return QueueBuilder.durable(Constants.PROCESSED_PAYMENTS_QUEUE).build();
	}

	@Bean
	TopicExchange processedPaymentsExchange() {
		return new TopicExchange(Constants.PROCESSED_PAYMENTS_EXCHANGE);
	}

	@Bean
	Binding processedPaymentsBinding() {
		return BindingBuilder.bind(processedPaymentsQueue()).to(processedPaymentsExchange())
				.with(Constants.PROCESSED_PAYMENTS_ROUTING_KEY);
	}

	// DLQ and DLX Configs
	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(Constants.DLQ_NAME).build();
	}

	@Bean
	TopicExchange deadLetterExchange() {
		return new TopicExchange(Constants.DLX_NAME);
	}

	@Bean
	Binding deadLetterBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(Constants.DLX_ROUTING_KEY);
	}

	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
