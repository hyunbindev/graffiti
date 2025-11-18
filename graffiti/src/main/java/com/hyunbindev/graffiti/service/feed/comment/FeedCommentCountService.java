package com.hyunbindev.graffiti.service.feed.comment;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedCommentCountService {
	private final RedisTemplate<String,Object> redisTemplate;
	private final String FEED_KEY_PREFIX = "feedComment:";
	/**
	 * 덧글 수 count up
	 * @param feedId
	 * @return
	 */
	@Async
	public void incrementCommentCount(Long feedId) {
		String key = FEED_KEY_PREFIX + feedId;
		redisTemplate.opsForValue().increment(key, 1L);
	}
	/**
	 * 덧글 수 count down
	 * @param feedId
	 */
	@Async
	public void decrementCommentCount(Long feedId) {
		String key = FEED_KEY_PREFIX + feedId;
		redisTemplate.opsForValue().decrement(key,1L);
	}
	/**
	 * 덧글 수 조회
	 * @param feedId
	 * @return
	 */
	public Long getCommentCount(Long feedId) {
		String key = FEED_KEY_PREFIX + feedId;
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		
		Long commentCount = ((Integer)values.get(key)).longValue();
		
		return commentCount;
	}
}
