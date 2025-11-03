package com.hyunbindev.graffiti.constant;

import lombok.Getter;

@Getter
public enum Role {
	GUEST("ROLE_GUEST"),
	MEMBER("ROLE_MEMBER"),
	ADMIN("ROLE_ADMIN");
	
	private final String key;
	
	Role(String key){
		this.key = key;
	}
}