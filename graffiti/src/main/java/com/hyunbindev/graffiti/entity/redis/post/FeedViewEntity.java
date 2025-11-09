package com.hyunbindev.graffiti.entity.redis.post;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.NoArgsConstructor;


@RedisHash(value = "feedView", timeToLive = 86_401)
@NoArgsConstructor
public class FeedViewEntity implements Serializable{
	
	private static final long serialVersionUID = -2913322640903298616L;

	@Id
	private Long feedId;
	
	//중복 방지
	private Set<String> memberUuids = new HashSet<>();
	
	/**
	 * 조회수 증가
	 * @param member
	 */
	public void increViewCount(String userUuid) {
		this.memberUuids.add(userUuid);
	}
	/**
	 * 조회 카운트 조회
	 * @return
	 */
	public long getViewCount() {
		return this.memberUuids.size();
	}
	
	public FeedViewEntity(Long feedId) {
		this.feedId = feedId;
	}
}