package com.hyunbindev.graffiti.data.secret;


import java.time.LocalDateTime;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.secret.SecretEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class SecretDTO {
	private boolean isAnswered;
	private Long id;
	private MemberInfoDTO author;
	private LocalDateTime createdAt;
	
	private String text;
	
	@Setter
	private Long viewCount;
	private Long commentCount;
	private Long likeCount;
	
	public static SecretDTO mappingDTO(SecretEntity entity, boolean isAnswered, long viewCount, long likeCount, long CommentCount) {
		return SecretDTO.builder()
				.isAnswered(isAnswered)
				.id(entity.getId())
				.author(new MemberInfoDTO(entity.getAuthor()))
				.createdAt(entity.getCreatedAt())
				.text(entity.getText())
				.viewCount(viewCount)
				.likeCount(likeCount)
				.commentCount(CommentCount)
				.build();
	}
	public static SecretDTO mappingNotAnswerDTO(SecretEntity entity, long viewCount, long likeCount, long CommentCount) {
		return SecretDTO.builder()
				.isAnswered(false)
				.id(entity.getId())
				.author(new MemberInfoDTO(entity.getAuthor()))
				.createdAt(entity.getCreatedAt())
				.viewCount(viewCount)
				.likeCount(likeCount)
				.commentCount(CommentCount)
				.build();
	}
}
