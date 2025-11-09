package com.hyunbindev.graffiti.data.whisper;

import java.time.LocalDateTime;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.FeedCommentEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WhisperCommentDTO {
	private Long id;
	private MemberInfoDTO author;
	private LocalDateTime createdAt;
	private String text;
	
	public static WhisperCommentDTO mappingDTO(FeedCommentEntity entity) {
		return WhisperCommentDTO.builder()
				.id(entity.getId())
				.author(new MemberInfoDTO(entity.getAuthor()))
				.createdAt(entity.getCreatedAt())
				.text(entity.getText())
				.build();
	}
	
}