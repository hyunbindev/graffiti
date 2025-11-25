package com.hyunbindev.graffiti.data.notification;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
public class MentionNotificationDTO implements Serializable{
	
	private static final long serialVersionUID = 853962263682477389L;

	@Getter
	private Long feedId;
	
	@Getter
	private MemberInfoDTO feedAuthor;
	
	@Getter
	private Boolean isInvisible;
	
	@Getter
	private List<String> receiverUuids;
	
	public static MentionNotificationDTO mappingDTO(WhisperEntity whisperEntity) {
		return MentionNotificationDTO.builder()
				.feedId(whisperEntity.getId())
				.feedAuthor(new MemberInfoDTO(whisperEntity.getAuthor()))
				.isInvisible(whisperEntity.isInvisibleMention())
				.receiverUuids(
				    Optional.ofNullable(whisperEntity.getMentionMembers())
				            .orElse(Collections.emptyList())
				            .stream()
				            .map(m -> m.getId())
				            .toList())
				.build();
	}
}
