package com.hyunbindev.graffiti.service.feed.view;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedViewService {
	
	@Qualifier("viewCountRedisTemplate")
	private final RedisTemplate<String, String> redisTemplate;
	private final JdbcTemplate jdbcTemplate;
	public long getViewCountAndSync(Long feedId, String userUuid) {
		String key = "feedView:"+ feedId;
		
		addViewInfo(key, userUuid);
		
		return redisTemplate.opsForSet().size(key);
	}
	/**
	 * 조회수 반영
	 * @param key
	 * @param userUuid
	 */
	@Async
	public void addViewInfo(String key, String userUuid) {
		redisTemplate.opsForSet().add(key, userUuid);
		redisTemplate.expire(key, 25, TimeUnit.HOURS);
	}
	/**
	 * 조회수 배치 로직
	 * @param viewCountMap
	 */
	@Transactional
	public void syncViewCount(Map<Long,Long> viewCountMap) {
        if (viewCountMap.isEmpty()) {
            return;
        }
        final String sql = "UPDATE feed_base_entity SET view_count = view_count + ? WHERE id = ?";
        
        List<Entry<Long, Long>> updates = viewCountMap.entrySet().stream().toList();
        
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Entry<Long, Long> entry = updates.get(i);
                ps.setLong(1, entry.getValue());
                ps.setLong(2, entry.getKey());   
            }
            @Override
            public int getBatchSize() {
                return updates.size();
            }
        });
	}
}