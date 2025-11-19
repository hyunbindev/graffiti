package com.hyunbindev.gateway.component;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtValidator {
	// 1. SecretKey와 JwtParser는 한 번만 생성하여 메모리에 보관합니다.
	private SecretKey secretKey;
	private JwtParser jwtParser;
	
	@Value("${jwt.access.secret-key}")
	private String accessSecretKey;

	@PostConstruct
	public void init() {
		byte[] keyBytes = accessSecretKey.getBytes(StandardCharsets.UTF_8);
		// HMAC Key 생성
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.jwtParser = Jwts.parser()
				.verifyWith(secretKey)
				.build();
		log.info("JWT Validator initialized successfully using key.");
	}
	
	public Claims validateToken(String accessToken) {
		// 검증 로직에서는 별도의 객체 생성을 최소화합니다.
		Claims claims = null;
		try {
			// 미리 생성된 jwtParser 인스턴스를 사용합니다.
			claims = jwtParser
					.parseSignedClaims(accessToken)
					.getPayload();
		//토큰 만료
		}catch(ExpiredJwtException exception) {
			log.error("Expired JWT Token: {}", exception.getMessage());
			throw exception;
		//시그니처 이상
		}catch(SignatureException exception) {
			log.error("Invalid JWT Signature: {}", exception.getMessage());
			throw exception;
		//변조된 token
		}catch(MalformedJwtException exception) {
			log.error("Malformed JWT Token (Invalid Format): {}", exception.getMessage());
			throw exception;
		}catch(Exception exception) {
			log.error("Unhandled JWT Validation Error: {}", exception.getMessage());
			// RuntimeException 등 처리되지 않은 예외에 대비하여 로그를 남기고 다시 던집니다.
			throw exception;
		}
		return claims;
	}
}