package com.jams.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jams.oauth.bean.AuthorizationCodeReq;
import com.jams.oauth.bean.AuthorizationCode;
import com.jams.oauth.bean.AccessTokenReq;
import com.jams.oauth.exception.InvalidRequestException;
import com.jams.oauth.bean.AccessToken;
import com.jams.oauth.service.OauthService;

@Controller

public class OauthController {

	@Autowired
	private OauthService oauthService;

	@RequestMapping("/authorize")

	public String authorize(@RequestParam(value = "response_type") String responseType,
			@RequestParam(value = "client_id") String clientId,
			@RequestParam(value = "redirect_uri") String redirectUri, @RequestParam(value = "state") String state,
			@RequestParam(value = "scope") String scope, RedirectAttributes redirectAttrs) {

		if ("token".equalsIgnoreCase(responseType)) {
			AccessTokenReq req = new AccessTokenReq();
			req.setClientId(clientId);
			req.setUserId(getAuthenticatedUser());
			AccessToken resp;
			try {
				resp = oauthService.implicitGrant(req);
				redirectAttrs.addAttribute("access_token", resp.getAccessToken());
				redirectAttrs.addAttribute("token_type", resp.getType());
				redirectAttrs.addAttribute("expires_in", resp.getExpiresIn());
				redirectAttrs.addAttribute("scope", scope);
				redirectAttrs.addAttribute("state", state);
			} catch (InvalidRequestException e) {
				redirectAttrs.addAttribute("error", "invalidRequest");
				redirectAttrs.addAttribute("state", state);
			}
			
		} else if ("code".equalsIgnoreCase(responseType)) {
			System.out.println(redirectUri);
			AuthorizationCodeReq req = new AuthorizationCodeReq();
			req.setClientId(clientId);
			req.setUserId(this.getAuthenticatedUser());
			AuthorizationCode resp;
			try {
				resp = oauthService.issueAuthorizedCode(req);
				redirectAttrs.addAttribute("code", resp.getCode());
				redirectAttrs.addAttribute("scope", scope);
				redirectAttrs.addAttribute("state", state);
			} catch (InvalidRequestException e) {
				redirectAttrs.addAttribute("error", "invalidRequest");
				redirectAttrs.addAttribute("state", state);
			}
		} 
		return "redirect:" + redirectUri;
	}

	@RequestMapping("/access_token")
	@ResponseBody
	public Object getAccessToken(@RequestParam(value = "grant_type") String grantType,
			@RequestParam(value = "code") String code,
			@RequestParam(value = "client_id") String clientId,
			@RequestParam(value = "client_secret") String clientSecret,
			@RequestParam(value = "redirect_uri") String redirectUri, 
			@RequestParam(value = "state") String state,
			@RequestParam(value = "scope") String scope, 
			RedirectAttributes redirectAttrs) {
		String message;
	
		if ("authorization_code".equalsIgnoreCase(grantType)) {
			//System.out.println(clientId);
			AccessTokenReq req = new AccessTokenReq();
			req.setClientId(clientId);
			//req.setUserId(getAuthenticatedUser());
			req.setScope(scope);
			AccessToken token;
			try {
				token = oauthService.issueAccessToken(code,req);
				return token;
			} catch (InvalidRequestException e) {
				message=makeErrorMessage("invalid_request:"+e.getDescription());
			}
		}else
			message=makeErrorMessage("invalid_request");
		return message;
	}
	
	private String getAuthenticatedUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()  
			    .getAuthentication().getPrincipal();
		String userName=(userDetails.getUsername());
		return userName;
	}
	
	private String makeErrorMessage(String reason) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("{\"error\":\"");
		buffer.append(reason);
		buffer.append("\"}");
		return buffer.toString();
	}

}
