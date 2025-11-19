package com.hyunbindev.graffiti.service.feed.comment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedCommentCountBatch{
	private final String FEED_COMMENT_PREFIX = "feedComment:";
	private final int CHUNK_SIZE = 1000;
	private final RedisTemplate<String,Object> redisTemplate;
	private final FeedCommentService feedCommentService;
	
	@Scheduled(initialDelay = 120000, fixedRate = 1800000)
	public void syncCommentCount() {
		long startTime = System.currentTimeMillis();
		
		ScanOptions options = ScanOptions.scanOptions()
				.match(FEED_COMMENT_PREFIX+"*")
				.count(CHUNK_SIZE)
				.build();
		Cursor<String> cursor = redisTemplate.scan(options);
		
		Map<Long,Integer> commentCountMap = new HashMap<>();
		try(cursor){
			Map<Long, Long> commenCountChunk = new HashMap<>();
			while(cursor.hasNext()) {
				String key = cursor.next();
				Long count = ((Integer)redisTemplate.opsForValue().get(key)).longValue();
				Long feedId = Long.parseLong(key.substring(FEED_COMMENT_PREFIX.length()));
				redisTemplate.delete(key);
				commenCountChunk.put(feedId, count);
				
				//청크 사이즈가 1000 개일 경우 나눠 배치 진행
				if(commentCountMap.size()<CHUNK_SIZE) continue;
				feedCommentService.syncCommentCount(commenCountChunk);
				commentCountMap.clear();
			}
			//1000개단위 나머지 배치 처리 
			feedCommentService.syncCommentCount(commenCountChunk);
		}catch(Exception e) {
			log.error("Comment 배치 처리 실패{}",e);
			//TODO-배치 처리 실패후 로직
		}finally{
			long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
			log.info("--- Feed Comment Count Sync Batch Finished in {}ms ---", duration);
		}
	}
}