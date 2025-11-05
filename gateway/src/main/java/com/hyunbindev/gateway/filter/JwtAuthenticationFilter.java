package com.hyunbindev.gateway.filter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunbindev.gateway.component.JwtValidator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
/**
 * jwt token validate and parse filter
 * @author hyunbinDev
 */
public class JwtAuthenticationFilter implements GlobalFilter{
	private final JwtValidator jwtValidator;
	private final ObjectMapper objectMapper;
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		log.info("request-path : ",path);
		if(path.startsWith("/api/oauth2") || path.startsWith("/api/v1/authentication")) return chain.filter(exchange);
		
		String token = exchange.getRequest().getHeaders().getFirst("Authorization");
		if(token == null || !token.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return onError(exchange.getResponse(), "Missing or invalid Authorization header.", HttpStatus.UNAUTHORIZED);
		}
		token = token.substring(7);
		try {
			Claims claims = jwtValidator.validateToken(token);
			String userUuid = claims.getSubject();
			String userNickname = claims.get("nickname", String.class);
			ServerWebExchange mutatedExchange = exchange.mutate().request(exchange.getRequest()
					.mutate()
					.header("X-User-UUID", userUuid)
					.header("X-User-NickName", userNickname)
					.build()).build();
			
			log.info("{}:{}",userUuid, userNickname);
			
			return chain.filter(mutatedExchange);
		}catch(ExpiredJwtException exception) {
			String reissueMessage = "Access token expired. Please use your refresh token to get a new access token.";
			return onError(exchange.getResponse(), reissueMessage, HttpStatus.UNAUTHORIZED);
		}catch(Exception exception) {
			return onError(exchange.getResponse(), "JWT Signature is invalid.", HttpStatus.UNAUTHORIZED);
		}
	}
	/**
     * WebFlux 환경에서 에러 발생 시 JSON 응답 본문과 상태 코드를 설정하는 헬퍼 메서드.
     * @param response ServerHttpResponse 객체
     * @param errorMsg 클라이언트에게 보낼 에러 메시지
     * @param httpStatus HTTP 상태 코드 (예: 401, 403)
     * @return Mono<Void>
     */
    private Mono<Void> onError(ServerHttpResponse response, String errorMsg, HttpStatus httpStatus) {
        response.setStatusCode(httpStatus); // HTTP 상태 코드 설정
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON); // Content-Type 설정
        
        // JSON 응답 본문 구조화
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", httpStatus.value());
        errorDetails.put("error", httpStatus.getReasonPhrase());
        errorDetails.put("message", errorMsg); 
        try {
            // Map 객체를 JSON 바이트 배열로 변환
            byte[] bytes = objectMapper.writeValueAsBytes(errorDetails);
            
            // WebFlux 논블로킹 방식으로 응답 본문에 작성
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
            
        } catch (JsonProcessingException e) {
            log.error("Error creating JSON response: {}", e.getMessage());
            // JSON 변환 실패 시 (비상 처리)
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }
    }
}
