package com.hyunbindev.graffiti.config.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitMQConfig {
	public static final String COMMENT_QUEUE_NAME = "graffiti.notification.comment.queue";
	public static final String COMMENT_ROUTING_KEY = "notification.comment.#";
	
	public static final String MENTION_QUEUE_NAME = "graffiti.notification.mention.queue";
	public static final String MENTION_ROUTING_KEY = "notification.mention.#";
	
	public static final String EXCHANGE_NAME = "graffiti.exchange.notification";
	
	
	@Bean("notificationCommentQueue")
	public Queue commentQueue() {
		return new Queue(COMMENT_QUEUE_NAME, true);
	}
	
	@Bean("notificationMentionQueue")
	public Queue mentionQueue() {
		return new Queue(MENTION_QUEUE_NAME, true);
	}
	
	@Bean("notificationExchange")
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}
	
	@Bean("commentBinding")
    public Binding commentBinding(@Qualifier("notificationCommentQueue") Queue queue, @Qualifier("notificationExchange")TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(COMMENT_ROUTING_KEY);
    }
	
	@Bean("mentionBinding")
    public Binding mentionBinding(@Qualifier("notificationMentionQueue") Queue queue, @Qualifier("notificationExchange")TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MENTION_ROUTING_KEY);
    }
}
