package com.graffiti.notification.data;


import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor
public class CommentNotificationDTO {
	@Getter
	private Long feedId;
	@Getter
	private String receiverUuid;
	@Getter
	private MemberInfoDTO commentAuthor;
	@Getter
	private String commentText;
}
