package com.hyunbindev.graffiti.data.post;

import java.time.LocalDateTime;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostPreViewDTO {
	private MemberInfoDTO authorInfo;
	private LocalDateTime createdAt;
	private String previewText;
}