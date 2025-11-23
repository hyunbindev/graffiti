package com.hyunbindev.graffiti.config.oauth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OauthFailHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
		
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setContentType("application/json;charset=UTF-8");
		
		log.error("ex : {}",exception);
		
		String responseBody = exception.getCause().getMessage();
		log.error("fail to oauth2 authentication : {}",responseBody);
	    int start = responseBody.indexOf("{");
	    int end = responseBody.lastIndexOf("}") + 1;
		
        String jsonPart = responseBody.substring(start, end);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonPart);

        String error = node.path("error").asText();
        String errorDescription = node.path("error_description").asText();
        String errorCode = node.path("error_code").asText();

        log.error("fail to Oauth2 : {}, description: {}, code: {}", error, errorDescription, errorCode);
        switch(errorCode) {
        	case "KOE237" :
        		response.getWriter().write("반복적인 로그인 요청을 하셨습니다. 잠시후 다시 시도해 주세요.");
        		break;
        	default :
        		response.getWriter().write("서버 내부 오류 잠시후 다시 시도해 주세요.");
        		break;
        }
	}
}