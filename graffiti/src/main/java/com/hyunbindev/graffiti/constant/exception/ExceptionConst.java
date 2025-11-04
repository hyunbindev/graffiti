package com.hyunbindev.graffiti.constant.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionConst {
	String getMessage();
	HttpStatus getStatus();
}