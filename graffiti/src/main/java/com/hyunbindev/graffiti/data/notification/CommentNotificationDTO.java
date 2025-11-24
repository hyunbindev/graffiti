package com.hyunbindev.graffiti.data.notification;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedCommentEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
public class CommentNotificationDTO {
	@Getter
	private Long feedId;
	@Getter
	private String receiverUuid;
	@Getter
	private MemberInfoDTO commentAuthor;
	@Getter
	private String commentText;
	
	public static CommentNotificationDTO mappingDTO(FeedBaseEntity feedEntity, FeedCommentEntity commentEntity) {
		return CommentNotificationDTO.builder()
				.feedId(feedEntity.getId())
				.receiverUuid(feedEntity.getAuthor().getId())
				.commentAuthor(new MemberInfoDTO(commentEntity.getAuthor()))
				.commentText(commentEntity.getText())
				.build();
	}
}