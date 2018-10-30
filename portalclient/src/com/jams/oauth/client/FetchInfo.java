package com.jams.oauth.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jams.oauth.bean.AccessToken;

@Controller

public class FetchInfo {

	@RequestMapping("/fetch")
	@ResponseBody
	public String fetchInfo(HttpSession httpSession,RedirectAttributes redirectAttrs) {

		final String ENDPOINT = "http://localhost:8888/info/config";

		String response = null;
		try {
			AccessToken token=(AccessToken)httpSession.getAttribute("accessToken");
			if(token==null) {
				token=OauthTokenTool.getAccessToken("john.carnell", "password1");
			}
			HttpPost httpPost;
			httpPost = new HttpPost(ENDPOINT);
			httpPost.addHeader("Authorization", "Bearer " + token.getAccess_token());
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());
			BufferedReader bufferedReader = new BufferedReader(reader);
			response = bufferedReader.readLine();
		
		} catch (Exception e) {
			System.out.println("Exception Occured!");
			e.printStackTrace();
		}
		return "Response From Resource Server:"+response;
	}
}
