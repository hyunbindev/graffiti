package com.hyunbindev.graffiti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hyunbindev.graffiti.config.filter.HeaderAuthenticationFilter;
import com.hyunbindev.graffiti.config.oauth.OauthFailHandler;
import com.hyunbindev.graffiti.config.oauth.OauthSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final OauthFailHandler oauthFailHandler;
	private final OauthSuccessHandler oauthSuccessHandler;
	private final HeaderAuthenticationFilter headerAuthenticationFilter;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf(CsrfConfigurer::disable)
		.formLogin(FormLoginConfigurer::disable)
		.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth->auth.requestMatchers("/api/oauth2/**").permitAll().requestMatchers("/error").permitAll().anyRequest().authenticated())
		.oauth2Login(customConfigurer-> customConfigurer
				.authorizationEndpoint(authorization->authorization.baseUri("/api/oauth2/authorization"))
				.failureHandler(oauthFailHandler)
				.successHandler(oauthSuccessHandler))
		.addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
