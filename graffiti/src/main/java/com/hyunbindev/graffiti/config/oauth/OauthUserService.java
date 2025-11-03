package com.hyunbindev.graffiti.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User defaultOauthUser = delegate.loadUser(userRequest);
		
		KakaoOauth2User kakaoOauth2user = new KakaoOauth2User(defaultOauthUser);
		
		log.info("{} : {} : assinged",kakaoOauth2user.getConnectedTime(),kakaoOauth2user.getName(),kakaoOauth2user.getNickName());
		
		return kakaoOauth2user;
	}
}
