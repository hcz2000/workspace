package com.jams.oauth.service;

import com.jams.oauth.bean.AuthorizationCodeReq;
import com.jams.oauth.bean.AuthorizationCode;
import com.jams.oauth.bean.AccessTokenReq;
import com.jams.oauth.exception.InvalidRequestException;
import com.jams.oauth.exception.InvalidToken;
import com.jams.oauth.bean.AccessToken;

public interface OauthService {
	public AccessToken implicitGrant(AccessTokenReq request) throws InvalidRequestException;
	public AuthorizationCode issueAuthorizedCode(AuthorizationCodeReq request) throws InvalidRequestException;
	public AccessToken issueAccessToken(String authCode,AccessTokenReq request) throws InvalidRequestException;
	public AccessToken getAccessToken(String tokenString) throws InvalidToken;
}
