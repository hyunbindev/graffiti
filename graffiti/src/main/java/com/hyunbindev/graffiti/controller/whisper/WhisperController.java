package com.hyunbindev.graffiti.controller.whisper;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hyunbindev.graffiti.constant.feed.FeedType;
import com.hyunbindev.graffiti.data.whisper.WhisperCreateDTO;
import com.hyunbindev.graffiti.data.whisper.WhisperDTO;
import com.hyunbindev.graffiti.service.image.ImageService;
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
	private final ImageService imageService;
	/**
	 * whisper feed 생성
	 * @param auth
	 * @param createDto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Long> createWhisperFeed(Authentication auth, @RequestPart("feed") @Valid WhisperCreateDTO createDto,@RequestPart(value="image", required=false)MultipartFile image){
		if(image!= null && 10 < image.getSize()) {
			String imageName = imageService.saveImage(FeedType.WHISPER.getFeedType()+"-"+UUID.randomUUID().toString(),image);
			return ResponseEntity.status(HttpStatus.CREATED).body(whisperService.createWhisperFeedWithImage(auth.getName(), createDto, imageName));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(whisperService.createWhisperFeed(auth.getName(), createDto));
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
	 * 
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
