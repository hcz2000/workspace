package com.jams.oauth.util;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.jams.oauth.bean.JWToken;

import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;

public class JWTUtil {
	private final static String JAMSIssuer="JAMS_AS";
	private final static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

	//static public String createJWT(String id, String issuer, String subject, long ttlMillis) {
	static public String createJWT(JWToken token) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setId(token.getUserId()).setIssuedAt(now).setSubject(token.getScope()).setIssuer(JAMSIssuer)
				.signWith(signatureAlgorithm, signingKey);
		builder.claim("client_id",token.getClientId());
		builder.claim("type", token.getType());
		builder.setExpiration(new Date(token.getExpiresMills()));
		
		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	static public JWToken parseJWT(String jwt) {
		// This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims=Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
				.parseClaimsJws(jwt).getBody();
		JWToken token=new JWToken();
		token.setUserId(claims.getId());
		token.setClientId((String)claims.get("client_id"));
		token.setType((String)claims.get("type"));
		token.setExpiresMills(claims.getExpiration().getTime());
		return token;
	}
	
	public static void main(String[] args) {
		JWToken token1=new JWToken();
		token1.setClientId("cient-001");
		token1.setUserId("hcz");
		token1.setType("bearer");
		token1.setScope("global");
		token1.setExpiresMills(System.currentTimeMillis()+3600000);
		String tokenString=createJWT(token1);
		JWToken token2=parseJWT(tokenString);
		System.out.println("UserId: " + token2.getUserId());
		System.out.println("ClientId: " + token2.getClientId());
		System.out.println("Expiration: " + new Date(token2.getExpiresMills()));
	}	
}
