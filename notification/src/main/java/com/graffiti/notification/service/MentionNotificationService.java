package com.graffiti.notification.service;

import org.springframework.stereotype.Service;

import com.graffiti.notification.constant.NotificationType;
import com.graffiti.notification.data.MentionNotificationDTO;
import com.graffiti.notification.entity.Notification;
import com.graffiti.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentionNotificationService {
	private final NotificationRepository notificationRepository;
	public void saveMentionNotification(MentionNotificationDTO notificationDTO) {
	    for(String mentionUuid : notificationDTO.getReceiverUuids()) {
	        Notification notification = Notification.builder()
	            .type(NotificationType.MENTION)
	            .sender(notificationDTO.getFeedAuthor())
	            .receiverUuid(mentionUuid)
	            .content(notificationDTO.isInvisible()
	                        ? "누군가 당신을 지목했어요"
	                        : notificationDTO.getFeedAuthor().getNickName() + "님이 당신을 언급했어요.")
	            .feedId(notificationDTO.isInvisible() ? null : notificationDTO.getFeedId())
	            .build();
	        log.info("{}",notificationDTO.getFeedId());
	        notificationRepository.save(notification);
	    }
	}
}
