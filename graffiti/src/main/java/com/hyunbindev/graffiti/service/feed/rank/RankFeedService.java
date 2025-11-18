package com.hyunbindev.graffiti.service.feed.rank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.feed.PostPreViewDTO;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.feed.FeedBaseRepository;
import com.hyunbindev.graffiti.repository.jpa.group.GroupRepository;
import com.hyunbindev.graffiti.service.feed.FeedService;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankFeedService {
	
	private final RedisTemplate<String,String> redisTemplate;
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
	private final FeedService feedService;
	private final FeedBaseRepository feedBaseRepository;
	private final String RANK_KEY_PATTERN = "rank_feed";
	/**
	 * 그룹 별 인기순 게시글 조회
	 * @param userUuid
	 * @param groupId
	 * @return
	 */
	public List<PostPreViewDTO> getRankFeedByGroup(String userUuid, String groupId, int page, int size){
		MemberEntity user = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		if(!user.isInGroup(group))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		String redisKey = RANK_KEY_PATTERN + ":" + groupId;
		
		Set<String> rankFeedIdsString = redisTemplate.opsForZSet()
				.reverseRange(redisKey, page*size, page * size + size-1);
		
		if (rankFeedIdsString == null || rankFeedIdsString.isEmpty()) {
	        return Collections.emptyList();
	    }
		List<Long> pagedFeedIds = rankFeedIdsString.stream()
				.map(Long::valueOf)
				.toList();
		List<FeedBaseEntity> feedEntitys = feedBaseRepository.findAllById(pagedFeedIds);
		return feedEntitys.stream().map((feed)-> feedService.mappingPreviewDto(feed, user)).toList();
	}
	/**
	 * 데이터 베이스에서 산출
	 * @param userUuid
	 * @param groupId
	 * @param page
	 * @param size
	 * @return
	 */
	public List<PostPreViewDTO> getRankFeedByGroupInDatabase(String userUuid, String groupId, int page, int size){
		MemberEntity user = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		if(!user.isInGroup(group))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		List<Long> feedIds = feedBaseRepository.findByRankFeed(groupId);
		List<FeedBaseEntity> feedBaseEntitys = feedBaseRepository.findAllById(feedIds);
		return feedBaseEntitys.stream().map((feed)-> feedService.mappingPreviewDto(feed, user)).toList();
	}
	
	/**
	 * 피드 랭크 테스트
	 */
	@Deprecated
	public void testInsert() {
		String redisKey = "rank_feed:4de92e18-aeeb-4a45-a3f1-a5c657fa4b86";
		
		Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
		
		for (long i = 1500208; i <= 1510208; i++) {
		    String member = String.valueOf(i);
		    Double score = (double) i;
		    ZSetOperations.TypedTuple<String> tuple = ZSetOperations.TypedTuple.of(member, score);
		    tuples.add(tuple);
		    log.debug("test no : {}",i);
		}
		redisTemplate.opsForZSet().add(redisKey, tuples);
	}
}
