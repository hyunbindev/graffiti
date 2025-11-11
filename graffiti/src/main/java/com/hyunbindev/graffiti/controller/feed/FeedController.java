package com.hyunbindev.graffiti.controller.feed;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.post.PostPreViewDTO;
import com.hyunbindev.graffiti.service.feed.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {
	private final FeedService feedService;
	@GetMapping
	public ResponseEntity<List<PostPreViewDTO>> getRecentPreviewPage(Authentication auth,@RequestParam(name="lastId", required=false) Long lastId, @RequestParam(name="size") int size){
		return ResponseEntity.ok(feedService.getRecentPostPreviewWithPage(auth.getName(), lastId, size));
	}
}