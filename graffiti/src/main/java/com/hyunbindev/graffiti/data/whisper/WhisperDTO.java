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
	/**
	 * DTO 매핑
	 * @param entity
	 * @return
	 */
	public static WhisperDTO mappingDTO(WhisperEntity entity, long viewCount, long likeCount, long commentCount) {
		return WhisperDTO.builder()
				.id(entity.getId())
				.author(new MemberInfoDTO(entity.getAuthor()))
				.createdAt(entity.getCreatedAt())
				.metionMembers(entity.getMentionMembers().stream().map((m)->new MemberInfoDTO(m)).toList())
				.viewCount(viewCount)
				.likeCount(likeCount)
				.commentCount(commentCount)
				.text(entity.getText())
				.build();
	}
}
