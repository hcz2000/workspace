package com.jams.oauth.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jams.oauth.bean.AccessToken;

@Controller

public class OauthTokenTool {

	static public AccessToken getAccessToken(String userName,String password) {

		final String TOKEN_ENDPOINT = "http://localhost:8888/oauth/token";
		final String GRANT_TYPE = "password";
		final String CLIENT_ID = "eagleeye";
		final String CLIENT_SECRET = "thisissecret";
		final String SCOPE="webclient";
		
		String response = null;
		AccessToken token = null;
		try {
			HttpPost httpPost;
			httpPost = new HttpPost(
					TOKEN_ENDPOINT + "?grant_type=" + URLEncoder.encode(GRANT_TYPE, StandardCharsets.UTF_8.name())
							+ "&username=" + URLEncoder.encode(userName, StandardCharsets.UTF_8.name())
							+ "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8.name())
							+ "&scope=" + URLEncoder.encode(SCOPE, StandardCharsets.UTF_8.name())
							);

			String clientCredentials = CLIENT_ID + ":" + CLIENT_SECRET;
			String encodedClientCredentials = new String(Base64.encodeBase64(clientCredentials.getBytes()));

			httpPost.setHeader("Authorization", "Basic " + encodedClientCredentials);
			// Make the access token request
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// Handle access token response
			Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());
			BufferedReader bufferedReader = new BufferedReader(reader);
			response = bufferedReader.readLine();
			if(!response.contains("error")) {
				  ObjectMapper objectMapper = new ObjectMapper();
				  token=objectMapper.readValue(response,AccessToken.class);
				  System.out.println(token.getAccess_token());
			}else
				System.out.println(response);

		} catch (Exception e) {
			System.out.println("Exception Occured!");
			e.printStackTrace();
		}
		return token;
	}
}
