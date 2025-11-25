package com.graffiti.notification.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.graffiti.notification.constant.NotificationType;
import com.graffiti.notification.data.MemberInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Document(collection = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
	@Id
	@Getter
	private String id;
	
	@Getter
	private NotificationType type;
	
	@Getter
	private MemberInfoDTO sender;
	
	@Getter
	private String content;
	
	@Getter
	private String receiverUuid;
	
	@Getter
	private boolean isRead;
	
	@Getter
	private Long feedId;
	
	@Builder.Default
	private LocalDateTime createdAt =LocalDateTime.now();
}
