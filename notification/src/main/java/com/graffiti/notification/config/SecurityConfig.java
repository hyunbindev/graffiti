package com.graffiti.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.graffiti.notification.config.filter.HeaderAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final HeaderAuthenticationFilter headerAuthenticationFilter;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf(CsrfConfigurer::disable)
		.formLogin(FormLoginConfigurer::disable)
		.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
