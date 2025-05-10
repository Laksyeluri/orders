package com.ecom.paymenthandler.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	public static final String PAYMENT_ROUTING_KEY = "payment";
	public static final String PAYMENT_FAILURE_ROUTING_KEY = PAYMENT_ROUTING_KEY + ".failure";
	public static final String PAYMENT_EXCHANGE = "payment_exchange";
	public static final String PAYMENT_FAILURE_QUEUE = "payment_failure_queue";

	public static final String PAYMENT_RETRY_EXCHANGE = "payment_retry_exchange";
	public static final String PAYMENT_RETRY_ROUTING_QUEUE = "payment_retry_queue";
	public static final String PAYMENT_RETRY_ROUTING_KEY = "payment_retry_routing_key";

	@Bean
	DirectExchange retryExchange() {
		return new DirectExchange(PAYMENT_RETRY_EXCHANGE);
	}

	@Bean
	TopicExchange paymentExchange() {
		return new TopicExchange(PAYMENT_EXCHANGE);
	}

	@Bean
	Queue retryQueue() {
		return QueueBuilder.durable(PAYMENT_RETRY_ROUTING_QUEUE).withArgument("x-message-ttl", 30000) // Retry after 30
																										// sec
				.withArgument("x-dead-letter-exchange", PAYMENT_EXCHANGE)
				.withArgument("x-dead-letter-routing-key", PAYMENT_ROUTING_KEY).build();
	}

	@Bean
	Queue paymentFailureQueue() {
		return QueueBuilder.durable(PAYMENT_FAILURE_QUEUE).build();
	}

	@Bean
	Binding retryBinding() {
		return BindingBuilder.bind(retryQueue()).to(retryExchange()).with(PAYMENT_RETRY_ROUTING_KEY);
	}

	@Bean
	Binding paymentFailureBinding() {
		return BindingBuilder.bind(paymentFailureQueue()).to(paymentExchange()).with(PAYMENT_FAILURE_ROUTING_KEY);
	}

	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
