package com.hyunbindev.graffiti.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hyunbindev.graffiti.exception.CommonAPIException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * @author hyunbinDev 공통 API Exception Handler
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(CommonAPIException.class)
	public ResponseEntity<Map<String,Object>> commonAPIExceptionHandler(CommonAPIException exception){
		Map<String,Object> exceptionBody = new HashMap<>();
		exceptionBody.put("message", exception.getMessage());
		exceptionBody.put("time", exception.getTime());
		
		if(exception.getDetail()!=null)exceptionBody.put("detail", exception.getDetail());
		
		return ResponseEntity.status(exception.getStatus()).body(exceptionBody);
	}
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String,Object>> notFoundExceptionHandler(Exception exception){
		Map<String,Object> exceptionBody = new HashMap<>();
		exceptionBody.put("time", LocalDateTime.now());
		exceptionBody.put("message", exception.getMessage());
		return ResponseEntity.ok(exceptionBody);
	}
}