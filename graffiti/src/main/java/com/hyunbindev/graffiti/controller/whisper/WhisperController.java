package com.hyunbindev.graffiti.controller.whisper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.whisper.WhisperCreateDTO;
import com.hyunbindev.graffiti.data.whisper.WhisperDTO;
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
	/**
	 * whisper feed 생성
	 * @param auth
	 * @param createDto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> createWhisperFeed(Authentication auth, @RequestBody @Valid WhisperCreateDTO createDto){
		whisperService.createWhisperFeed(auth.getName(), createDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	/**
	 * whisper feed 삭제
	 * @param auth
	 * @param feedId
	 * @return
	 */
	@DeleteMapping("/{whisperId}")
	public ResponseEntity<Void> deleteWhisperFeed(Authentication auth, @PathVariable("whisperId")Long feedId){
		whisperService.deleteWhisperFeed(auth.getName(), feedId);
		return ResponseEntity.accepted().build();
	}
	/**
	 * whisper feed 조회
	 * @param auth
	 * @param feedId
	 * @return
	 */
	@GetMapping("/{whisperId}")
	public ResponseEntity<WhisperDTO> getWhisperFeed(Authentication auth, @PathVariable("whisperId")Long feedId){
		return ResponseEntity.ok(whisperService.getWhisperFeed(auth.getName(), feedId));
	}
}
