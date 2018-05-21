package com.jams.oauth.bean;

public class AccessTokenReq {
	private String grantType;
	private String scope;
	private String clientId;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grant_type) {
		this.grantType = grant_type;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String code) {
		this.scope = code;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String client_id) {
		this.clientId = client_id;
	}
	

}
