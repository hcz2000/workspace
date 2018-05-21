package com.jams.oauth.controller;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jams.oauth.bean.ErrorInfo;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if(body instanceof String){
			return new ResponseEntity<Object>(new ErrorInfo((String)body,status),headers,status);
		}
		
		return  new ResponseEntity<Object>(new ErrorInfo(ex,status),headers,status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex,"The requested body is not readable!", headers, HttpStatus.BAD_REQUEST, request);
	}


	@Override
    @ResponseBody
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex,"The requested parameters is missing!", headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex,"The requested parameters is not valid!", headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({NullPointerException.class})
	protected ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
		return handleExceptionInternal(ex,"An internal error occur during the requeste!", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
}
