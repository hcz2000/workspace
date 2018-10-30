package com.jams.oauth.client;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomizedRestTemplate extends RestTemplate{
	
	 public CustomizedRestTemplate(){
	    	super();
	    	List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
	    	if(interceptors==null) {
	    		interceptors=Collections.singletonList((ClientHttpRequestInterceptor)new UserContextInterceptor());
	    		setInterceptors(interceptors);
	        } else {
	            interceptors.add(new UserContextInterceptor());
	            setInterceptors(interceptors);
	        }
	    	
	    }
	 
	 public class UserContextInterceptor implements ClientHttpRequestInterceptor {
			    @Override
		    public ClientHttpResponse intercept(
		            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
		            throws IOException {

		        HttpHeaders headers = request.getHeaders();
		        headers.add("Authorization", "Bearer "+UserContext.getAuthToken());
		      
		        return execution.execute(request, body);
		    }
		}

}
