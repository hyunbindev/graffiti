package com.hyunbindev.graffiti.constant.exception;

import org.springframework.http.HttpStatus;
/**
 * CommonAPIException 상수 인터페이스
 * @author hyunbinDev
 */
public interface ExceptionConst {
	String getMessage();
	HttpStatus getStatus();
}