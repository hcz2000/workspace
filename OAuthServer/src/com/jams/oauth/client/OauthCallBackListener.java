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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jams.oauth.bean.AccessToken;

@Controller

public class OauthCallBackListener {

	@RequestMapping("/callback")
	@ResponseBody
	public String callback(@RequestParam(value = "code") String authorizationCode, HttpSession httpSession,RedirectAttributes redirectAttrs) {

		final String TOKEN_ENDPOINT = "http://localhost:8080/oauth/api/access_token";
		final String GRANT_TYPE = "authorization_code";
		final String REDIRECT_URI = "http://localhost:8080/oauth/clientapp/callback";
		final String CLIENT_ID = "client-0001";
		final String CLIENT_SECRET = "testpass";
		final String STATE="mystate";
		final String SCOPE="GLOBAL";
		
		String response = null;
		try {
			HttpPost httpPost;
			httpPost = new HttpPost(
					TOKEN_ENDPOINT + "?grant_type=" + URLEncoder.encode(GRANT_TYPE, StandardCharsets.UTF_8.name())
							+ "&code=" + URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8.name())
							+ "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8.name())
							+ "&client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8.name())
							+ "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8.name())
							+ "&state=" + URLEncoder.encode(STATE, StandardCharsets.UTF_8.name())
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
				  AccessToken token=objectMapper.readValue(response,AccessToken.class);
				  httpSession.setAttribute("accessToken", token);
				  System.out.println(token.getAccessToken());
			}

		} catch (Exception e) {
			System.out.println("Exception Occured!");
			e.printStackTrace();
		}
		return "Response From Oauth Server:"+response;
	}
}
