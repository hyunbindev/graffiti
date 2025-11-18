package com.hyunbindev.graffiti.config.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter{
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		//metrics 수집 url filter 예외
		if(request.getRequestURI().startsWith("/actuator/prometheus")) {
			filterChain.doFilter(request, response);
			return;
		}
		//사용자 식별 uuid 파싱
		String userUuid = request.getHeader("X-User-UUID");
		String userNickName = request.getHeader("X-User-NickName");
		
		//log.info("{} : {} : {}",request.getRequestURI(),userUuid, userNickName);
		
		if (userUuid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//user principal
			User principal = new User(userUuid, "", Collections.emptyList());
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			//spring security context 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
}
