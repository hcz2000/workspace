package com.jams.authentication.controllers;

import java.util.HashMap;
import java.util.Map;

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
}
