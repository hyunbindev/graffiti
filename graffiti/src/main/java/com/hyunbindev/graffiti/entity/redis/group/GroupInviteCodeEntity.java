package com.hyunbindev.graffiti.entity.redis.group;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RedisHash(value = "groupInviteCode", timeToLive = 86_400)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Setter
@Getter
public class GroupInviteCodeEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6919272755847445844L;
	@Id
	private String code;
	private String groupName;
	private String groupUuid;
}
