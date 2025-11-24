package com.graffiti.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Slf4j
public class NotificationController {
	
	@GetMapping
	public ResponseEntity<Void> getNotification(Authentication auth){
		return ResponseEntity.ok().build();
	}
}
