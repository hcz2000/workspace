package com.jams.authentication.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.jams.authentication.repository.UserOrganizationRepository;


public class JWTTokenEnhancer implements TokenEnhancer {
	
    @Autowired
    private UserOrganizationRepository orgRepository;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		Map<String, Object> additionalInfo = new HashMap<String,Object>();
        additionalInfo.put("organizationId", orgRepository.findByUserName(authentication.getName()).getOrganizationId());
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}

}
