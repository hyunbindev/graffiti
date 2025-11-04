package com.hyunbindev.gateway.component;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtValidator {
	@Value("${jwt.access.secret-key}")
	private String accessSecretKey;
	
	//access token 검증
	public Claims validateToken(String accessToken) {
		byte[] keyBytes = accessSecretKey.getBytes(StandardCharsets.UTF_8);
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
		Claims clames = null;
		try {
			clames = Jwts.parser()
					.verifyWith(secretKey)
					.build()
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
			throw exception;
		}
		return clames;
	}
}
