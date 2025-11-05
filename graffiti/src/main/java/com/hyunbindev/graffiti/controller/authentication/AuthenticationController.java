package com.hyunbindev.graffiti.controller.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.service.authentication.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
@Slf4j
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	
	@GetMapping("/token/reissue")
	public ResponseEntity<String> reissueAccessToken(
			@RequestHeader("Authorization")
			String accessToken,
			
			@CookieValue(value = "refreshToken", required = true)
			String refreshToken){
		return ResponseEntity.ok(authenticationService.reissueAccessToken(accessToken, refreshToken));
	}
}
