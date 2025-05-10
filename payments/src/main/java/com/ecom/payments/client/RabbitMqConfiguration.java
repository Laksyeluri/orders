package com.ecom.payments.client;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecom.payments.util.Constants;

@Configuration
public class RabbitMqConfiguration {

	// https://spring.io/guides/gs/messaging-rabbitmq
	@Bean
	Queue queue() {
		return QueueBuilder.durable(Constants.PAYMENT_QUEUE_NAME).deadLetterExchange(Constants.DLX_EXCHANGE_NAME)
				.deadLetterRoutingKey(Constants.DLX_ROUTING_KEY).build();
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(Constants.PAYMENT_EXCHANGE_NAME);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(Constants.PAYMENT_ROUTING_KEY);
	}
}
