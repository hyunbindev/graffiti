package com.hyunbindev.graffiti.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hyunbindev.graffiti.exception.CommonAPIException;

import lombok.extern.slf4j.Slf4j;

/**
 * 전역 Exception Handler
 * @author hyunbinDev
 */
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
		
		exceptionBody.put("time", exception.getTime());
		exceptionBody.put("message", exception.getMessage());
		
		if(exception.getDetail()!=null)exceptionBody.put("detail", exception.getDetail());
		
		return ResponseEntity.status(exception.getStatus()).body(exceptionBody);
	}
	/**
	 * not found Exception Handler
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String,Object>> notFoundExceptionHandler(Exception exception){
		Map<String,Object> exceptionBody = new HashMap<>();
		
		exceptionBody.put("time", LocalDateTime.now());
		exceptionBody.put("message", exception.getMessage());
		
		return ResponseEntity.ok(exceptionBody);
	}
	/**
	 * bad argument request Exception Handler
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,Object>> badArgumentException(MethodArgumentNotValidException exception){
		Map<String,Object> exceptionBody = new HashMap<>();
		
		String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Invalid request");
		
		exceptionBody.put("time",LocalDateTime.now());
		exceptionBody.put("message", message);
		return ResponseEntity.badRequest().body(exceptionBody);
	}
}