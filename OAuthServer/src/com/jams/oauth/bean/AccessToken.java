package com.jams.oauth.bean;

public class AccessToken {
	private String clientId;
	private String userId;
	private String type;
	private String scope;
	private String access_token;
	private long remind_in;
	private long expires_in;
	private String refresh_token;
	
	public AccessToken() {
		super();
	}
	public AccessToken(String clientId, String userId, String type, String access_token, long remind_in,
			long expires_in, String refresh_token, String scope) {
		super();
		this.clientId = clientId;
		this.userId = userId;
		this.type = type;
		this.access_token = access_token;
		this.remind_in = remind_in;
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
		this.scope = scope;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRefreshToken() {
		return refresh_token;
	}
	public void setRefreshToken(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getAccessToken() {
		return access_token;
	}
	public void setAccessToken(String sccess_token) {
		this.access_token = sccess_token;
	}
	public long getRemindIn() {
		return remind_in;
	}
	public void setRemindIn(long remind_in) {
		this.remind_in = remind_in;
	}
	public long getExpiresIn() {
		return expires_in;
	}
	public void setExpiresIn(long expires_in) {
		this.expires_in = expires_in;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
}
