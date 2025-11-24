package com.hyunbindev.graffiti.service.feed.rank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.repository.jpa.feed.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.group.GroupRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedRankBatch {
	private final FeedBaseRepository feedBaseRepository;
	private final GroupRepository groupRepository;
	private final RedisTemplate<String,String> redisTemplate;
	private final String RANK_KEY_PATTERN = "rank_feed";
	private final int GROUP_BATCH_SIZE = 500;
	/**
	 * 6시간 주기 인기 게시글 배치 처리
	 */
	@Scheduled(initialDelay = 0, fixedRate = 21600000)
	@Transactional(readOnly=true)
	public void storeRankFeed() {
		List<GroupEntity> groups = groupRepository.findAll();
		
		groups.stream().forEach((group)->{
			String groupId = group.getId();
			List<RankScoreDTO> results = feedBaseRepository.getRankFeedByGroupId(groupId, GROUP_BATCH_SIZE);
			
			Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
			results.stream().forEach((result)->{
				ZSetOperations.TypedTuple<String> tuple = ZSetOperations.TypedTuple.of(result.getFeedId().toString(), result.getScore());
				tuples.add(tuple);
			});
			//게시글이 없어 결과 값이 비어 있을 시
			if(tuples.isEmpty()) return;
			
			redisTemplate.opsForZSet().add(RANK_KEY_PATTERN+":"+group.getId(), tuples);
			redisTemplate.expire(RANK_KEY_PATTERN+":"+group.getId(), 7, TimeUnit.HOURS);
		}); 
	}
	//feedId score DTO
	public static class RankScoreDTO{
		@Getter
		private final Long feedId;
		@Getter
		private final Double score;
		
		public RankScoreDTO(Long feedId, Double score) {
			this.feedId=feedId;
			this.score=score; 
		}
	}
}
