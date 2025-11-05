package com.hyunbindev.graffiti.service.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.component.JwtTokenParser;
import com.hyunbindev.graffiti.component.JwtTokenProvider;
import com.hyunbindev.graffiti.constant.exception.AuthenticationExceptionConst;
import com.hyunbindev.graffiti.entity.redis.authenticaiton.RefreshTokenEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.redis.authentication.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenParser jwtTokenParser;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Value("${jwt.refresh.validity}")
	private long refreshTokenValidity;
	/**
	 * 엑세스 토큰 재발급
	 * @param accessToken
	 * @param refreshToken
	 * @return
	 */
	public String reissueAccessToken(String accessToken, String refreshToken) {
		
		if(accessToken == null || !accessToken.startsWith("Bearer ")) {
			throw new CommonAPIException(AuthenticationExceptionConst.INVALID_JWT_TOKEN);
		}
		accessToken = accessToken.substring(7);
		
		Claims claims = jwtTokenParser.getAccessTokenClaims(accessToken);
		
		
		RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(claims.getSubject())
				.orElseThrow(()-> new CommonAPIException(AuthenticationExceptionConst.NO_VALIDITY));
		
		
		//사용자 쿠키의 refresh token과 저장된 토큰이 같지 않을경우 exception throw
		if(!jwtTokenParser.getRfreshTokenClaims(refreshToken).getSubject().equals(jwtTokenParser.getRfreshTokenClaims(refreshTokenEntity.getRefreshToken()).getSubject()))
			throw new CommonAPIException(AuthenticationExceptionConst.INVALID_JWT_TOKEN);
		
		log.info("Refreshed AccesToken : {}",jwtTokenParser.getRfreshTokenClaims(refreshToken).getSubject());
		
		return "Bearer "+jwtTokenProvider.refreshAccessToken(claims);
	}
	
	public void logOut(String accessToken) {
		
	}
	/**
	 * 리프레시 토큰 저장
	 * @param userUuid
	 * @param refreshToken
	 */
	public void saveRefreshToken(String userUuid, String refreshToken) {
		RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
				.id(userUuid)
				.refreshToken(refreshToken)
				.ttl(refreshTokenValidity/1000)
				.build();//new RefreshTokenEntity(userUuid, refreshToken, refreshTokenValidity);
		refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
	}
}