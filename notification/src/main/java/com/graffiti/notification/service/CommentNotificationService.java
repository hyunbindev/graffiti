package com.graffiti.notification.service;

import org.springframework.stereotype.Service;

import com.graffiti.notification.constant.NotificationType;
import com.graffiti.notification.data.CommentNotificationDTO;
import com.graffiti.notification.entity.Notification;
import com.graffiti.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentNotificationService {
	private final NotificationRepository notificationRepository; 
	
	public void saveCommentNotification(CommentNotificationDTO notificationDTO){
		Notification notification = Notification.builder()
				.type(NotificationType.COMMENT)
				.isRead(false)
				.content(notificationDTO.getCommentAuthor().getNickName()+"님이 게시글에 덧글을 작성했어요!")
				.sender(notificationDTO.getCommentAuthor())
				.receiverUuid(notificationDTO.getReceiverUuid())
				.feedId(notificationDTO.getFeedId())
				.build();
		
		notification = notificationRepository.save(notification);
		log.info(notification.getId());
	}
}