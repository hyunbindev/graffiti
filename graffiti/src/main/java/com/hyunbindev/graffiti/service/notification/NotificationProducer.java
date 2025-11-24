package com.hyunbindev.graffiti.service.notification;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.config.rabbitMQ.NotificationRabbitMQConfig;
import com.hyunbindev.graffiti.data.notification.CommentNotificationDTO;
import com.hyunbindev.graffiti.data.notification.MentionNotificationDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
	private final RabbitTemplate rabbitTemplate;
	/**
	 * 덧글 작성 이벤트 발행
	 * @param message
	 */
	public void pubCommentNotification(CommentNotificationDTO message) {
		rabbitTemplate.convertAndSend(NotificationRabbitMQConfig.EXCHANGE_NAME,NotificationRabbitMQConfig.COMMENT_ROUTING_KEY,message);
	}
	/**
	 * 언급 이벤트 발행
	 * @param message
	 */
	public void pubMentionNotification(MentionNotificationDTO message) {
		rabbitTemplate.convertAndSend(NotificationRabbitMQConfig.EXCHANGE_NAME,NotificationRabbitMQConfig.MENTION_ROUTING_KEY,message);
	}
}
