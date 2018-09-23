package com.jams.licenses;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import com.jams.licenses.utils.UserContextInterceptor;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableResourceServer
public class Application {
	
    /*
	@Bean
    public RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }*/
	
	@Primary
    @Bean
    public RestTemplate getCustomizedRestTemplate(){
    	RestTemplate template=new RestTemplate();
    	List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
    	if(interceptors==null) {
    		interceptors=Collections.singletonList((ClientHttpRequestInterceptor)new UserContextInterceptor());
    		template.setInterceptors(interceptors);
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
    	
    	return template;
    }
	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}

}
