package com.jams.oauth.infosvr.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;



public class InfoControllerTest {
	private final String accessToken="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJoY3oiLCJpYXQiOjE1MjMxNTM2NTksInN1YiI6IkdMT0JBTCIsImlzcyI6IkpBTVNfQVMiLCJjbGllbnRfaWQiOiJjbGllbnQtMDAwMSIsInR5cGUiOiJiZWFyZXIiLCJleHAiOjE1MjMxNTcyNTl9.Km1Tucd7MRXQpcxS1BAtEumHb0g-9MqIEEyav6RCYt0";
	  
    @Test  
    public void testJsonResponse() throws IOException, URISyntaxException {
        String url = "http://localhost:8080/oauth/infosvr/fetch_info?userName="+URLEncoder.encode("hcz", StandardCharsets.UTF_8.name());  
  
        ClientHttpRequest request = new SimpleClientHttpRequestFactory().createRequest(new URI(url), HttpMethod.GET);  
        //request.getHeaders().set("Accept", "application/json");  
		request.getHeaders().set("Authorization", "Bearer " + accessToken);
        
            
        ClientHttpResponse response = request.execute();
        HttpStatus status=response.getStatusCode();
        
        if(status.OK.equals(status)) {
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
        }else {
        	System.out.println("Http Status:"+status);
        }
        
   
    }  
    
    @Test  
    public void testJsonResponse2() throws IOException, URISyntaxException {
        String url = "http://localhost:8080/oauth/infosvr/fetch_info?userName="+URLEncoder.encode("hcz", StandardCharsets.UTF_8.name());  
  
        ClientHttpRequest request = new SimpleClientHttpRequestFactory().createRequest(new URI(url), HttpMethod.GET);  
		request.getHeaders().set("Authorization", "Bearer " + accessToken);
        
		String response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Authorization", "Bearer " + accessToken);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			System.out.println(httpResponse.getStatusLine());
			Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());
			BufferedReader bufferedReader = new BufferedReader(reader);
			response = bufferedReader.readLine();
		
		} catch (Exception e) {
			System.out.println("Exception Occured!");
			e.printStackTrace();
		}
		System.out.println(response);
    }  
}