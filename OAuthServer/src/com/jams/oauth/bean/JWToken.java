package com.jams.oauth.bean;

public class JWToken {
	private String clientId;
	private String userId;
	private String type;
	private String scope;
	private long expiresMills;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public long getExpiresMills() {
		return expiresMills;
	}
	public void setExpiresMills(long expiresMills) {
		this.expiresMills = expiresMills;
	}
}
