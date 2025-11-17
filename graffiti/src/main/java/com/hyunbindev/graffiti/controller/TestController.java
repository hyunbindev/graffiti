package com.hyunbindev.graffiti.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.service.feed.rank.RankFeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
	@GetMapping
	public ResponseEntity<String> testAPI(){
		return ResponseEntity.ok("test ok!");
	}
}
