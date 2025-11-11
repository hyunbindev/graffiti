package com.hyunbindev.graffiti.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitWebpConvertQueueConfig {
	@Bean("webpContvertQeue")
	public Queue webpConvertQueue() {
		return new Queue(Config.QUEUE.getValue(),true);
	}
	
	@Bean("webpConvertExchange")
	public TopicExchange webpConvertExchange() {
		return new TopicExchange(Config.EXHANGE.getValue());
	}
	@Bean
	public Binding notificationBinding(@Qualifier("webpContvertQeue")Queue queue, @Qualifier("webpConvertExchange")TopicExchange exchange) {
		return BindingBuilder.bind(queue)
				.to(exchange)
				.with(Config.ROUTING.getValue());
	}
	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
	    return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	    RabbitTemplate template = new RabbitTemplate(connectionFactory);
	    template.setMessageConverter(jackson2JsonMessageConverter());
	    return template;
	}
	
	public static enum Config{
		QUEUE("WEBP_CONVERT"),
		EXHANGE("WEBP_CONVERT.EXCHANGE"),
		ROUTING("WEBP_CONVERT.ROUTING");
		
		public final String value;
		
		Config(String value){
			this.value=value;
		}
		public String getValue() {
			return this.value;
		}
	}
}
