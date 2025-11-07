package com.hyunbindev.graffiti.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.hyunbindev.graffiti.constant.exception.ExceptionConst;

import lombok.Getter;
/**
 * CommonAPIException
 * API 공통 예외처리 Exception
 * 
 * @author hyunbinDev
 */
public class CommonAPIException extends RuntimeException{
	private static final long serialVersionUID = 5690884301286501022L;
	@Getter
	private final LocalDateTime time;
	@Getter
	private final HttpStatus status;
	@Getter
	private Map<String,String> detail;

	public CommonAPIException(String message, HttpStatus status) {
		super(message);
		this.status = status;
		this.time = LocalDateTime.now();
	}
	
	public CommonAPIException(String message, HttpStatus status, Map<String,String>detail) {
		super(message);
		this.status = status;
		this.time = LocalDateTime.now();
		this.detail = detail;
	}
	
	public CommonAPIException(ExceptionConst constant) {
		super(constant.getMessage());
		this.status= constant.getStatus();
		this.time = LocalDateTime.now();
	}

	public CommonAPIException(ExceptionConst constant, Map<String,String> detail) {
		super(constant.getMessage());
		this.status= constant.getStatus();
		this.time = LocalDateTime.now();
		this.detail = detail;
	}
}