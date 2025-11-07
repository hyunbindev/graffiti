package com.hyunbindev.graffiti.controller.whisper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.whisper.WhisperCreateDTO;
import com.hyunbindev.graffiti.service.whisper.WhisperService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/whisper")
@Slf4j
public class WhisperController {
	private final WhisperService whisperService;
	@PostMapping
	public ResponseEntity<Void> createWhisperPost(Authentication auth, @RequestBody @Valid WhisperCreateDTO createDto){
		whisperService.createWhisperFeed(auth.getName(), createDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
