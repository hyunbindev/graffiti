package com.hyunbindev.graffiti.controller.feed;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.feed.PostPreViewDTO;
import com.hyunbindev.graffiti.service.feed.FeedService;
import com.hyunbindev.graffiti.service.feed.rank.RankFeedService;
import com.hyunbindev.graffiti.service.feed.search.FeedSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {
	private final FeedService feedService;
	private final RankFeedService feedRankService;
	private final FeedSearchService feedSearchService;
	/**
	 * 최신글 조회
	 * @param auth
	 * @param lastId
	 * @param size
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<PostPreViewDTO>> getRecentPreviewPage(Authentication auth,@RequestParam(name="lastId", required=false) Long lastId, @RequestParam(name="size") int size){
		return ResponseEntity.ok(feedService.getRecentPostPreviewWithPage(auth.getName(), lastId, size));
	}
	
	/**
	 * 인기글 상위 10개 조회
	 * @param auth
	 * @param groupId
	 * @return
	 */
	@GetMapping("/rank")
	public ResponseEntity<List<PostPreViewDTO>> getRankPreviewPage(
			Authentication auth,
			@RequestParam(name="groupId", required=true) String groupId ,
			@RequestParam(name="page", required=true)int page,
			@RequestParam(name="size", required=true)int size){
		return ResponseEntity.ok(feedRankService.getRankFeedByGroup(auth.getName(), groupId, page, size));
	}
	/**
	 * 키워드 매칭 게시글 조회
	 * @param auth
	 * @param groupId
	 * @param size
	 * @param lastId
	 * @param keyWord
	 * @return
	 */
	@GetMapping("/search")
	public ResponseEntity<List<PostPreViewDTO>> getPreviewByKeyWord(
			Authentication auth,
			@RequestParam(name="groupId", required=true) String groupId,
			@RequestParam(name="size", required=true)Integer size,
			@RequestParam(name="lastId", required=true)Long lastId,
			@RequestParam(name="keyWord", required=true)String keyWord){
		return ResponseEntity.ok(feedSearchService.searchFeedByKeyWord(auth.getName(),groupId, keyWord,  size, lastId));
	}
} 