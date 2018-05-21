package com.jams.oauth.exception;

public class InvalidRequestException extends Exception{
	private String description;
	public InvalidRequestException(String desc) {
		description=desc;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
