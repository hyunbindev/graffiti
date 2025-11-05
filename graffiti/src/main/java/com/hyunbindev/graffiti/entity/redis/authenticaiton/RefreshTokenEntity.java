package com.hyunbindev.graffiti.entity.redis.authenticaiton;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@RedisHash("refreshToken")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Setter
@Getter
public class RefreshTokenEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3948558618521094655L;
	@Id
	private String id;
	private String refreshToken;
	
	@TimeToLive
	private long ttl;
}