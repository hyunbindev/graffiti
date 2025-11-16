package com.hyunbindev.graffiti.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.service.feed.RankFeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
	private final RankFeedService rankFeedService;
	@GetMapping
	public ResponseEntity<String> testAPI(){
		rankFeedService.testInsert();
		return ResponseEntity.ok("test ok!");
	}
}
