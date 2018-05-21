package com.jams.oauth.infosvr;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jams.oauth.bean.AccessToken;
import com.jams.oauth.bean.JWToken;
import com.jams.oauth.exception.InvalidToken;
import com.jams.oauth.service.OauthService;
import com.jams.oauth.util.JWTUtil;

@Controller

public class InfoController {
	/*
	@Autowired
	private OauthService oauthService;
	*/

	@RequestMapping("/fetch_info")
	@ResponseBody
	public Object getResource(@RequestParam(value = "userName") String userName,
			@RequestHeader(value="Authorization") String authHeader,
			RedirectAttributes redirectAttrs) {
		
		int index=authHeader.indexOf("Bearer ");
		String tokenString=authHeader.substring(index+7);
		
		JWToken token=JWTUtil.parseJWT(tokenString);
		if(System.currentTimeMillis()>token.getExpiresMills() ) {
			return makeErrorMessage("Token expired"); 
		}
	
/* verify accesstoken via stored data
		AccessToken token;
		try {
			token = oauthService.getAccessToken(tokenString);
		} catch (InvalidToken e) {
			return makeErrorMessage("Invalid token");
		}
		
		if(System.currentTimeMillis()>token.getExpiresIn() ) {
			return makeErrorMessage("Token expired"); 
		}*/
		
		return token;
	}
	
	private String makeErrorMessage(String reason) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("{\"error\":\"");
		buffer.append(reason);
		buffer.append("\"");
		return buffer.toString();
	}

}