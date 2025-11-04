package com.hyunbindev.graffiti.constant.exception;

import org.springframework.http.HttpStatus;

public enum MemberExceptionConst implements ExceptionConst{
	NOT_FOUND("회원을 찾을 수 없습니다.",HttpStatus.NOT_FOUND);
	
	private final String message;
	private final HttpStatus status;
	
	MemberExceptionConst(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
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
