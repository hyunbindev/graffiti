package com.hyunbindev.graffiti.constant.exception;

import org.springframework.http.HttpStatus;

/**
 * Whisper 게시글에 대한 Exception 상수
 * @author hyunbinDev
 */
public enum WhisperExceptionConst implements ExceptionConst{
	NOT_FOUND_FEED("잘못된 게시글 조회 입니다.",HttpStatus.NOT_FOUND),
	NOT_FOUND_COMMENT("잘못된 덧글 조회 입니다.",HttpStatus.NOT_FOUND),
	FORBIDDEN("비공개 게시글 입니다.", HttpStatus.FORBIDDEN);
	
	private final String message;
	private final HttpStatus status;
	
	WhisperExceptionConst(String message, HttpStatus status){
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
