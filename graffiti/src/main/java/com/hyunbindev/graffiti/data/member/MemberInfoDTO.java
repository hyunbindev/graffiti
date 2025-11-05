package com.hyunbindev.graffiti.data.member;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;

import lombok.Getter;

@Getter
public class MemberInfoDTO {
	private String uuid;
	private String nickName;
	private String profileImg;
	
	public MemberInfoDTO(MemberEntity memberEntity) {
		this.uuid = memberEntity.getId();
		this.nickName = memberEntity.getNickName();
		this.profileImg = memberEntity.getProfileImg();
	}
}