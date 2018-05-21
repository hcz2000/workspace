package com.jams.oauth.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.jams.oauth.bean.AuthorizationCodeReq;
import com.jams.oauth.bean.JWToken;
import com.jams.oauth.bean.AuthorizationCode;
import com.jams.oauth.bean.AccessTokenReq;
import com.jams.oauth.exception.InvalidRequestException;
import com.jams.oauth.exception.InvalidToken;
import com.jams.oauth.util.JWTUtil;
import com.jams.oauth.bean.AccessToken;

/**
 * @author Admin
 *
 */
@Service
public class OauthServiceImpl implements OauthService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Map<String, AuthorizationCode> issuedCodes = new HashMap<String, AuthorizationCode>();
	private final String sqlLoadToken = "SELECT user,client,type,scope,remind_in,expire_in,access_token,refresh_token FROM t_token WHERE access_token=?";
	private final RowMapper<AccessToken> tokenRowMapper;

	public OauthServiceImpl() {
		tokenRowMapper = new RowMapper<AccessToken>() {
			@Override
			public AccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new AccessToken(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getLong(5), rs.getLong(6), rs.getString(7), rs.getString(8));
			}
		};
	}

	@Override
	public AccessToken implicitGrant(AccessTokenReq request) {
		String code = gererateToken(request.getClientId(), request.getUserId() ,request.getScope());;
		AccessToken token = new AccessToken();
		token.setClientId(request.getClientId());
		token.setUserId(request.getUserId());
		token.setType("bearer");
		token.setAccessToken(code);
		token.setExpiresIn(System.currentTimeMillis() + 3600000);
		String scope = request.getScope();
		if (scope != null)
			token.setScope(scope);
		storeToken(token);
		return token;
	}

	@Override
	public AuthorizationCode issueAuthorizedCode(AuthorizationCodeReq request) {
		String code = gererateCode(request.getClientId(), request.getUserId());
		AuthorizationCode authCode = new AuthorizationCode();
		authCode.setClientId(request.getClientId());
		authCode.setUserId(request.getUserId());
		authCode.setCode(code);
		issuedCodes.put(code, authCode);
		String scope = request.getScope();
		if (scope != null)
			authCode.setScope(scope);
		return authCode;
	}

	@Override
	public AccessToken issueAccessToken(String authCode, AccessTokenReq request) throws InvalidRequestException {
		AuthorizationCode auth = issuedCodes.get(authCode);
		if (auth == null || !auth.getClientId().equals(request.getClientId()))
			throw new InvalidRequestException("Illegal authorization code");
		request.setUserId(auth.getUserId());
		issuedCodes.remove(authCode);
		String code = gererateToken(request.getClientId(), request.getUserId() ,request.getScope());
		AccessToken token = new AccessToken();
		token.setClientId(request.getClientId());
		token.setUserId(request.getUserId());
		token.setType("bearer");
		token.setAccessToken(code);
		token.setExpiresIn(System.currentTimeMillis() + 3600000);
		String scope = request.getScope();
		if (scope != null)
			token.setScope(scope);
		storeToken(token);
		return token;
	}

	private String gererateCode(String clientId, String userId) {
		String prime = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int len = prime.length();
		String code = "";
		for (int i = 0; i < 10; i++) {
			int loc = (int) Math.round(Math.floor((len - 1) * Math.random()));
			code = code + prime.charAt(loc);
		}
		return code;
	}

	private String gererateToken(String clientId, String userId,String scope) {
		JWToken token=new JWToken();
		token.setClientId(clientId);
		token.setUserId(userId);
		token.setType("bearer");
		token.setScope(scope);
		token.setExpiresMills(System.currentTimeMillis()+3600000);
		return JWTUtil.createJWT(token);
	}

	private void storeToken(AccessToken token) {
		jdbcTemplate.update(
				"insert into t_token(user,client,type,scope,remind_in,expire_in,access_token,refresh_token) values(?,?,?,?,?,?,?,?)",
				new Object[] { token.getUserId(), token.getClientId(), token.getType(), token.getScope(),
						token.getRemindIn(), token.getExpiresIn(), token.getAccessToken(), token.getRefreshToken() });
	}

	@Override
	public AccessToken getAccessToken(String tokenString) throws InvalidToken {
		try {
			AccessToken tokenFromQuery = jdbcTemplate.queryForObject(
					sqlLoadToken,tokenRowMapper, tokenString);
			return tokenFromQuery;
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidToken("Invalid token");
		}
	}
}
