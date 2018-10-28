package com.jams.authentication.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    @RequestMapping("/public")
    public String  publicInfo() {
        return "Public Info";
    }
    
    @RequestMapping("/config")
    public String  configInfo() {
        return "Config Info";
    }
}
