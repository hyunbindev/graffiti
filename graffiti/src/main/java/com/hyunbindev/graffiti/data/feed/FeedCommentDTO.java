package com.hyunbindev.graffiti.data.feed;

import java.time.LocalDateTime;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.FeedCommentEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FeedCommentDTO {
	private Long id;
	private MemberInfoDTO author;
	private LocalDateTime createdAt;
	private String text;
	
	public static FeedCommentDTO mappingDTO(FeedCommentEntity entity) {
		return FeedCommentDTO.builder()
				.id(entity.getId())
				.author(new MemberInfoDTO(entity.getAuthor()))
				.createdAt(entity.getCreatedAt())
				.text(entity.getText())
				.build();
	}
	
}