package com.jams.oauth.exception;

public class InvalidToken extends Exception{
	private String description;
	public InvalidToken(String desc) {
		description=desc;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
