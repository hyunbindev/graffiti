package com.hyunbindev.graffiti.data.notification;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
public class MentionNotificationDTO {
	@Getter
	private Long feedId;
	
	@Getter
	private MemberInfoDTO feedAuthor;
	
	@Getter
	private boolean isInvisible;
	
	@Getter
	private List<String> receiverUuids;
	
	public static MentionNotificationDTO mappingDTO(WhisperEntity whisperEntity) {
		return MentionNotificationDTO.builder()
				.feedId(whisperEntity.getId())
				.feedAuthor(new MemberInfoDTO(whisperEntity.getAuthor()))
				.isInvisible(whisperEntity.isInvisibleMention())
				.receiverUuids(
				    Optional.ofNullable(whisperEntity.getMentionMembers())
				            .orElse(Collections.emptyList()) // null이면 빈 리스트
				            .stream()
				            .map(m -> m.getId())
				            .toList())
				.build();
	}
}
