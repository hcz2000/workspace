package com.jams.oauth.bean;

public class AuthorizationCode {
	private String clientId;
	private String userId;
	private String scope;
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	//Optional
	private String state;
	public String getScope() {
		return scope;
	}
	public void setScope(String type) {
		this.scope = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
