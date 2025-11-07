package com.hyunbindev.graffiti.data.whisper;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WhisperCreateCommentDTO {
	@NotBlank(message="내용을 입력해 주세요")
	private String text;
}