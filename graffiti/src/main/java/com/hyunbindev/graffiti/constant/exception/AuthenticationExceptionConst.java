package com.hyunbindev.graffiti.constant.exception;

import org.springframework.http.HttpStatus;

public enum AuthenticationExceptionConst implements ExceptionConst{
	INVALID_JWT_TOKEN("잘못된 인증 정보 입니다.",HttpStatus.UNAUTHORIZED),
	NO_VALIDITY("인증시간이 만료되었습니다. 다시 로그인 해주세요",HttpStatus.UNAUTHORIZED);
	
	private final String message;
	private final HttpStatus status;
	
	AuthenticationExceptionConst(String message, HttpStatus status){
		this.message=message;
		this.status=status;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public HttpStatus getStatus() {
		return this.status;
	}
}
