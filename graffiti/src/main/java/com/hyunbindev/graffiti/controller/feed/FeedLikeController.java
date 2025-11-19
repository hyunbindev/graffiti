package com.hyunbindev.graffiti.controller.feed;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.feed.FeedLikeDTO;
import com.hyunbindev.graffiti.service.feed.like.FeedLikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
@Slf4j
public class FeedLikeController {
	private final FeedLikeService feedLikeService;
	
	@PostMapping("/{feedId}/like")
	public ResponseEntity<FeedLikeDTO> createFeedComment(Authentication auth, @PathVariable("feedId")Long feedId){
		return ResponseEntity.ok(feedLikeService.likeFeed(auth.getName(), feedId));
	}
}