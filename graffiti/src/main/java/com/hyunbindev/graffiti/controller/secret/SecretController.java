package com.hyunbindev.graffiti.controller.secret;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.data.secret.CreateSecretDTO;
import com.hyunbindev.graffiti.data.secret.SecretDTO;
import com.hyunbindev.graffiti.service.secret.SecretService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SecretController {
	private final SecretService secretService;
	
	/**
	 * 조회
	 * @param auth
	 * @param feedId
	 * @return
	 */
	@GetMapping("/v1/secret/{feedId}")
	public ResponseEntity<SecretDTO> getSecretFeed(Authentication auth, @PathVariable("feedId")Long feedId){
		return ResponseEntity.ok(secretService.getSecretFeed(auth.getName(), feedId));
	}
	
	/**
	 * 정답과 함께 조회
	 * @param auth
	 * @param feedId
	 * @param answer
	 * @return
	 */
	@GetMapping("/v1/secret/{feedId}/answer")
	public ResponseEntity<SecretDTO> getSecretFeedWithAnswer(Authentication auth, @PathVariable("feedId")Long feedId,@RequestParam("answer")String answer){
		return ResponseEntity.ok(secretService.getSecretFeedWithAnswer(auth.getName(), feedId, answer));
	}
	/**
	 * 피드 생성
	 * @param auth
	 * @param createDto
	 * @return
	 */
	@PostMapping("/v1/secret")
	public ResponseEntity<Void> createSecretFeed(Authentication auth, @RequestBody CreateSecretDTO createDto){
		secretService.createSecretFeed(auth.getName(), createDto);
		return ResponseEntity.ok().build();
	}
	/**
	 * 삭제
	 * @param auth
	 * @param feedId
	 * @return
	 */
	@DeleteMapping("/v1/secret/{feedId}")
	public ResponseEntity<MemberInfoDTO> deleteSecretFeed(Authentication auth,@PathVariable("feedId")Long feedId){
		secretService.deleteSecretFeed(auth.getName(), feedId);
		return ResponseEntity.ok().build();
	}
}
