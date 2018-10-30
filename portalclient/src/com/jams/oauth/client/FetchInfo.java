package com.jams.oauth.client;

import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jams.oauth.bean.AccessToken;

@Controller
public class FetchInfo {
	@Autowired
	private CustomizedRestTemplate restTemplate;

	@RequestMapping("/fetch")
	@ResponseBody
	public String fetchInfo(HttpSession httpSession,RedirectAttributes redirectAttrs) {

		final String serviceUri = "http://localhost:8888/info/config";

		AccessToken token=(AccessToken)httpSession.getAttribute("accessToken");
		if(token==null)
			token=OauthTokenTool.getAccessToken("john.carnell", "password1");
		UserContext.setAuthToken(token.getAccess_token());
		//CustomizedRestTemplate restTemplate=new CustomizedRestTemplate();
		ResponseEntity<Map> restExchange = restTemplate.exchange(serviceUri, HttpMethod.GET, null,Map.class);
			
		return "Response From Resource Server:"+restExchange.getBody().toString();
	}
	
}
