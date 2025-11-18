package com.hyunbindev.graffiti.service.feed.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedViewCountBatch {
	private final RedisTemplate<String,String> redisTemplate;
	private final String FEED_VIEW_PREFIX = "feedView:";
	private final int CHUNK_SIZE = 1000;
	private final FeedViewService feedViewService;
	
	@Scheduled(cron = "0 0 0/6 * * *")
	public void syncViewCount() {

		long startTime = System.currentTimeMillis();
		log.info("--- Feed View Count Sync Batch Started ---");
		Set<String> keys = redisTemplate.keys(FEED_VIEW_PREFIX+"*");
		
		
		ScanOptions options = ScanOptions.scanOptions()
				.match(FEED_VIEW_PREFIX+"*")
				.count(CHUNK_SIZE)
				.build();
		
		Cursor<String> cursor = redisTemplate.scan(options);
		Map<Long,Integer> viewCountMap = new HashMap<>();
		
		try(cursor){
			Map<Long, Long> feedViewsChunk = new HashMap<>();
			while(cursor.hasNext()) {
				String key = cursor.next();
				Long count = redisTemplate.opsForSet().size(key);
				Long feedId = Long.parseLong(key.substring(FEED_VIEW_PREFIX.length()));
				redisTemplate.delete(key);
				feedViewsChunk.put(feedId, count);
				
				//청크 사이즈가 1000 개일 경우 나눠 배치 진행
				if(viewCountMap.size()<CHUNK_SIZE) continue;
				feedViewService.syncViewCount(feedViewsChunk);
				viewCountMap.clear();
			}
			//1000개단위 나머지 배치 처리
			feedViewService.syncViewCount(feedViewsChunk);
		}catch(Exception e) {
			log.error("ViewCount 배치 처리 실패{}",e);
			//TODO-배치 처리 실패후 로직
		}finally{
			long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
			log.info("--- Feed View Count Sync Batch Finished in {}ms ---", duration);
		}
		redisTemplate.delete(keys);
	}
}
