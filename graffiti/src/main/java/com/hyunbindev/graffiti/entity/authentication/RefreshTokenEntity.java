package com.hyunbindev.graffiti.entity.authentication;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RedisHash("refreshToken")
@NoArgsConstructor
@Slf4j
public class RefreshTokenEntity implements Serializable{
	
	@Id
	private String userUuid;
	@Getter
	private String refreshToken;
	
	@TimeToLive
	private long ttl;
	
	public RefreshTokenEntity(String userUuid, String refreshToken, long ttl) {
		this.userUuid = userUuid;
		this.refreshToken = refreshToken;
		this.ttl = ttl/1000;
	}
}