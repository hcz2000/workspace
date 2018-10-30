package com.jams.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import com.jams.authentication.config.ServiceConfig;

@Configuration
public class JWTTokenStoreConfig {
	@Autowired 
	private ServiceConfig serviceConfig;
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices=new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
		/*
	    KeyStoreKeyFactory keyStoreKeyFactory =new KeyStoreKeyFactory(new ClassPathResource("config/mystore.jks"), "mypass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mykey"));
        
        Resource resource = new ClassPathResource("config/publickey.txt");
        String publicKey = null;
        try {
                publicKey = inputStream2String(resource.getInputStream());
        } catch (final IOException e) {
                throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);

		*/
		converter.setSigningKey(serviceConfig.getJwtSigningKey());
		return converter;
	}
	
	@Bean
	public TokenEnhancer jwtTokenEnhancer() {
		return new JWTTokenEnhancer();
	}
}
