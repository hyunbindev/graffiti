package com.hyunbindev.graffiti.data.member;

import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinGroupsDTO {
	String uuid;
	String name;
	
	public static JoinGroupsDTO mappingDTO(GroupEntity entity) {
		return JoinGroupsDTO.builder()
				.uuid(entity.getId())
				.name(entity.getName())
				.build();
	}
}
