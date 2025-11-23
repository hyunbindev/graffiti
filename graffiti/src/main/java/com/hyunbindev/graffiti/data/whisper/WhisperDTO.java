package com.hyunbindev.graffiti.data.whisper;

import java.time.LocalDateTime;
import java.util.List;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class WhisperDTO {
	private Long id;
	private MemberInfoDTO author;
	private LocalDateTime createdAt;
	
	private List<MemberInfoDTO> metionMembers;
	
	private String text;
	
	@Setter
	private Long viewCount;
	private Long commentCount;
	private Long likeCount;
	private Boolean isLiked;
	private boolean blinded;
	private String imageUrl;
	
	private boolean isMentionInvisible;
	
	/**
	 * DTO 매핑
	 * @param entity
	 * @return
	 */
	public static WhisperDTO mappingDTO(WhisperEntity entity, long viewCount, long likeCount, long commentCount, boolean isLiked) {
		return WhisperDTO.builder()
				.id(entity.getId())
				.author(new MemberInfoDTO(entity.getAuthor()))
				.createdAt(entity.getCreatedAt())
				.metionMembers(entity.getMentionMembers().stream().map((m)->new MemberInfoDTO(m)).toList())
				//캐싱된 조회수 및 저장된 조회수 합산
				.viewCount(viewCount + entity.getViewCount())
				.likeCount(likeCount)
				.isLiked(isLiked)
				.commentCount(commentCount)
				.text(entity.getText())
				.isMentionInvisible(entity.isInvisibleMention())
				.build();
	}
	public static WhisperDTO mappingDTOWithImage(WhisperEntity entity, long viewCount, long likeCount, long commentCount ,String imageUrl, boolean isLiked) {
		return WhisperDTO.builder()
				.id(entity.getId())
				.author(new MemberInfoDTO(entity.getAuthor()))
				.createdAt(entity.getCreatedAt())
				.metionMembers(entity.getMentionMembers().stream().map((m)->new MemberInfoDTO(m)).toList())
				//캐싱된 조회수 및 저장된 조회수 합산
				.viewCount(viewCount + entity.getViewCount())
				.likeCount(likeCount)
				.isLiked(isLiked)
				.commentCount(commentCount)
				.text(entity.getText())
				.isMentionInvisible(entity.isInvisibleMention())
				.imageUrl(imageUrl)
				.build();
	}
	public static WhisperDTO blindDTO() {
		return WhisperDTO.builder()
				.blinded(true)
				.build();
	}
}
