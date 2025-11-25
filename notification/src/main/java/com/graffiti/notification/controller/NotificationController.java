package com.graffiti.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graffiti.notification.data.NotificationDTO;
import com.graffiti.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Slf4j
public class NotificationController {
	private final NotificationService notificationService;
	@GetMapping
	public ResponseEntity<List<NotificationDTO>> getNotification(Authentication auth){
		return ResponseEntity.ok(notificationService.getRecentNotification(auth.getName()));
	}
}
