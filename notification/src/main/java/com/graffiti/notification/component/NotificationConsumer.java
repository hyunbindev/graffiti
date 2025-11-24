package com.graffiti.notification.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.graffiti.notification.config.NotificationRabbitMQConfig;
import com.graffiti.notification.data.CommentNotificationDTO;
import com.graffiti.notification.data.MentionNotificationDTO;
import com.graffiti.notification.service.CommentNotificationService;
import com.graffiti.notification.service.MentionNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
	private final CommentNotificationService commentNotificationService;
	private final MentionNotificationService mentionNotificationService;
	@RabbitListener(queues = NotificationRabbitMQConfig.COMMENT_QUEUE_NAME)
	public void receiveCommentNotificationMessage(CommentNotificationDTO message) {
		commentNotificationService.saveCommentNotification(message);
	}
	
	@RabbitListener(queues = NotificationRabbitMQConfig.MENTION_QUEUE_NAME)
	public void receiveMentionNotificationMessage(MentionNotificationDTO message) {
		mentionNotificationService.saveMentionNotification(message);
	}
}
