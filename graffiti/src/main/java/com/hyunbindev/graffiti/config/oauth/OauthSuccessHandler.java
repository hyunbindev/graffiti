package com.hyunbindev.graffiti.config.oauth;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.hyunbindev.graffiti.component.JwtTokenProvider;
import com.hyunbindev.graffiti.service.authentication.AuthenticationService;
import com.hyunbindev.graffiti.service.member.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OauthSuccessHandler implements AuthenticationSuccessHandler  {
	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationService authenticationService;
	@Value("${client.baseUrl}")
	private String redirectClientUrl;
	/**
	 * oauth 성공 이후 처리
	 * 회원 가입 처리 및 jwttoken 발급
	 * @author hyunbinDev
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		KakaoOauth2User  kakaoAuthentication = new KakaoOauth2User(oauthToken.getPrincipal());
		kakaoAuthentication = memberService.assignMember(kakaoAuthentication);
		
		String accessToken = "Bearer "+ jwtTokenProvider.generateAccessToken(kakaoAuthentication);
		String refreshToken = jwtTokenProvider.generateRefreshToken(kakaoAuthentication);
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setMaxAge((int) (jwtTokenProvider.getRefreshValidity() / 1000));
		
		String redirectUrl = UriComponentsBuilder.fromUriString(redirectClientUrl+"/callback")
				.queryParam("accessToken", URLEncoder.encode(accessToken, StandardCharsets.UTF_8.toString()))
				.build()
				.toUriString();
		authenticationService.saveRefreshToken(kakaoAuthentication.getMemberUuid(), refreshToken);
		response.sendRedirect(redirectUrl);
	}
}