package com.hyunbindev.graffiti.data.post;

import java.time.LocalDateTime;
import java.util.List;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class WhisperPreViewDTO extends PostPreViewDTO{
	List<MemberInfoDTO> mentionMember;
}
