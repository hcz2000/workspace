package com.jams.authentication.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    @RequestMapping("/public")
    public String  publicInfo() {
        return "Public Info";
    }
 
    @RequestMapping("/config")
    public Map<String,String>  configInfo() {
    	Map<String,String> config=new HashMap<String,String>();
    	config.put("spring.datasource.url", "jdbc:mysql://localhost:3306/test");
    	config.put("spring.datasource.username", "root");
    	config.put("spring.datasource.password", "");
        return config;
    }
    
    @RequestMapping(value = { "/user" }, produces = "application/json")
    public Map<String, Object> user(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return userInfo;
    }
    
}
