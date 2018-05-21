package com.jams.oauth.bean;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
	public String error;
	public String description;
	public int status;
	
	public ErrorInfo(Throwable throwable,HttpStatus status){
		this.error=throwable.getMessage();
		this.status=status.value();
	}
	public ErrorInfo(String message,HttpStatus status){
		this.error=message;
		this.status=status.value();
	}

}
