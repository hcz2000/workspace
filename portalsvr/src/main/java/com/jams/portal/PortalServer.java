package com.jams.portal;

import org.springframework.boot.SpringApplication;

public class PortalServer {
	public static void main(String[] args) {
		//Object servers[]= {com.jams.authentication.AuthenticationServer.class,com.jams.configsvr.ConfigServer.class};
		Object servers[]= {com.jams.authentication.AuthenticationServer.class};
		SpringApplication.run(servers,args);
	}

}