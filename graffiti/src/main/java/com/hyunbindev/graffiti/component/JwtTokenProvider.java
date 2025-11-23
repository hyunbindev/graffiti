package com.hyunbindev.graffiti.component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hyunbindev.graffiti.config.oauth.KakaoOauth2User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	//access token secret key
	@Value("${jwt.access.secret-key}")
	private String accessSecretKey;
	
	//access token validity
	@Value("${jwt.access.validity}")
	private long accessValidity;
	
	//refresh token secret key
	@Value("${jwt.refresh.secret-key}")
	private String refreshSecretKey;
	
	@Value("${jwt.refresh.validity}")
	private long refreshValidity;
	
	@SuppressWarnings("deprecation")
	public String generateAccessToken(KakaoOauth2User auth) {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		
		Date now = new Date();
		Date expireDate = new Date();
		expireDate.setTime(now.getTime()+accessValidity);
		Map<String, Object> payLoad = new HashMap<>();
		//사용자 닉네임
		payLoad.put("nickname", auth.getNickName());
		//사용자 권한
		payLoad.put("role", auth.getAuthorities());
		return Jwts.builder()
				.setHeader(header)
				.setClaims(payLoad)
				.setSubject(auth.getMemberUuid())
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256, accessSecretKey.getBytes())
				.compact();
	}
	@SuppressWarnings("deprecation")
	public String refreshAccessToken(Claims claims) {
		Date now = new Date();
		Date expireDate = new Date();
		expireDate.setTime(now.getTime()+accessValidity);
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(claims.getSubject())
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256, accessSecretKey.getBytes())
				.compact();
	}
	
	@SuppressWarnings("deprecation")
	public String generateRefreshToken(KakaoOauth2User auth) {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		
		Date now = new Date();
		Date expireDate = new Date();
		expireDate.setTime(now.getTime()+refreshValidity);
		Map<String, Object> payLoad = new HashMap<>();
		
		payLoad.put("nickname", auth.getNickName());
		
		return Jwts.builder()
				.setHeader(header)
				.setClaims(payLoad)
				.setSubject(auth.getMemberUuid())
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256, refreshSecretKey.getBytes())
				.compact();
	}
	
	public long getRefreshValidity() {
		return this.refreshValidity;
	}
	
	public long getAccessTokenValiditiy() {
		return this.accessValidity;
	}
}