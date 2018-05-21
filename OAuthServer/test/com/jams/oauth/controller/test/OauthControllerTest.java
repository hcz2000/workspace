package com.jams.oauth.controller.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class OauthControllerTest {  
	  
    @Test  
    public void testJsonResponse() throws IOException, URISyntaxException {
        String url = "http://localhost:8080/oauth/api/authorize?response_type=code&client_id=client-0001&scope=public_profile&state=mystate&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth%2Fclient%2Fcallback.html";  
  
        ClientHttpRequest request = new SimpleClientHttpRequestFactory().createRequest(new URI(url), HttpMethod.GET);  
        request.getHeaders().set("Accept", "application/json");  
        
    	String clientCredentials = "hcz" + ":" + "654321";
		String encodedClientCredentials = new String(Base64.encodeBase64(clientCredentials.getBytes()));
		request.getHeaders().set("Authorization", "Basic " + encodedClientCredentials);
        
        ClientHttpResponse response = request.execute();
        InputStream is = response.getBody();
        
        long contentLength=response.getHeaders().getContentLength();
        String jsonData =null;
        if(contentLength>0){
        	byte bytes[] = new byte[(int) response.getHeaders().getContentLength()];  
            is.read(bytes);  
            jsonData = new String(bytes);  
        }else{
        	BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            jsonData= sb.toString();
        }
        
        System.out.println(jsonData);  
    }  
}