package com.hyunbindev.graffiti.component;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hyunbindev.graffiti.constant.exception.AuthenticationExceptionConst;
import com.hyunbindev.graffiti.exception.CommonAPIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenParser{
	@Value("${jwt.access.secret-key}")
	private String accessSecretKey;
	
	@Value("${jwt.refresh.secret-key}")
	private String refreshSecretKey;
	/**
	 * 엑세스 토큰으로부터 subject 추출
	 * @param accessToken
	 * @return
	 */
	public Claims getAccessTokenClaims(String accessToken) {
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
			return clames;
		//시그니처 이상
		}catch(Exception exception) {
			log.debug(exception.getMessage());
			throw new CommonAPIException(AuthenticationExceptionConst.INVALID_JWT_TOKEN);
		}
		return clames;
	}
	/**
	 * 리프레시 토큰으로부터 subject 추출
	 * @param refreshToken
	 * @return
	 */
	public String getRefreshTokenSubject(String refreshToken) {
		byte[] keyBytes = refreshSecretKey.getBytes(StandardCharsets.UTF_8);
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
		Claims clames = null;
		try {
			clames = Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(refreshToken)
					.getPayload();
		}catch(Exception exception) {
			throw new CommonAPIException(AuthenticationExceptionConst.INVALID_JWT_TOKEN);
		}
		return clames.getSubject();
	}
	public Claims getRfreshTokenClaims(String refreshToken) {
		byte[] keyBytes = refreshSecretKey.getBytes(StandardCharsets.UTF_8);
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
		Claims clames = null;
		try {
			clames = Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(refreshToken)
					.getPayload();
		//토큰 만료
		}catch(ExpiredJwtException exception) {
			return clames;
		//시그니처 이상
		}catch(Exception exception) {
			log.debug(exception.getMessage());
			throw new CommonAPIException(AuthenticationExceptionConst.INVALID_JWT_TOKEN);
		}
		return clames;
	}
}