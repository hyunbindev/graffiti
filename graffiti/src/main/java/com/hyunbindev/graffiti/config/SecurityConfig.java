package com.hyunbindev.graffiti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.hyunbindev.graffiti.config.oauth.OauthFailHandler;
import com.hyunbindev.graffiti.config.oauth.OauthSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final OauthFailHandler oauthFailHandler;
	private final OauthSuccessHandler oauthSuccessHandler;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf(CsrfConfigurer::disable)
		.formLogin(FormLoginConfigurer::disable)
		.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		
		.authorizeHttpRequests(auth->auth.requestMatchers("/api/**").permitAll().anyRequest().authenticated())
		
		.oauth2Login(customConfigurer-> customConfigurer
				.authorizationEndpoint(authorization->authorization.baseUri("/api/oauth2/authorization"))
				.failureHandler(oauthFailHandler)
				.successHandler(oauthSuccessHandler));
		return http.build();
	}
}
