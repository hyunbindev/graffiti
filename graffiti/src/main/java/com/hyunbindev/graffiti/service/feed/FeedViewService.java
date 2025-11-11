package com.hyunbindev.graffiti.service.feed;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedViewService {
	private final RedisTemplate<String, String> redisTemplate;
	
	public long getViewCountAndSync(Long feedId, String userUuid) {
		String key = "feedView:"+ feedId;
		
		addViewInfo(key, userUuid);
		
		return redisTemplate.opsForSet().size(key);
	}
	
	@Async
	public void addViewInfo(String key, String userUuid) {
		redisTemplate.opsForSet().add(key, userUuid);
		redisTemplate.expire(key, 25, TimeUnit.HOURS);
	}
}
