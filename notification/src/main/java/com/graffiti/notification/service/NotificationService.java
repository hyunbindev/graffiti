package com.graffiti.notification.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.graffiti.notification.data.NotificationDTO;
import com.graffiti.notification.entity.Notification;
import com.graffiti.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final NotificationRepository notificationRepository;
	public List<NotificationDTO> getRecentNotification(String userUuid){
		
		Pageable pageable = PageRequest.of(0,20,Sort.by(Sort.Direction.DESC,"createdAt"));
		List<Notification> documents = notificationRepository.findByReceiverUuid(userUuid, pageable);
		
		
		
		
		return documents.stream()
			    .map(n -> NotificationDTO.builder()
			        .feedId(n.getFeedId())
			        .content(n.getContent())
			        .iconUrl(Optional.ofNullable(n.getSender())
			                         .map(sender -> sender.getProfileImg())
			                         .orElse(null))
			        .build())
			    .toList();
	}
}
