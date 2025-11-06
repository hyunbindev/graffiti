package com.hyunbindev.graffiti.data.whisper;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WhisperCreateDTO {
	@NotBlank(message="내용을 입력해 주세요")
	private String text;
	
	@NotBlank(message="그룹은 필수 선택 항목 입니다.")
	private String groupUuid;
	
	@NotNull(message="잘못된 사용자 언급 요청 입니다.")
	private List<String> mentionMembers;
	
	private boolean invisibleMention;
}
