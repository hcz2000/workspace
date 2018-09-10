package com.jams.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.jams.organization.utils.UserContextFilter;
import javax.servlet.Filter;

@SpringBootApplication
//@EnableEurekaClient
@EnableResourceServer
public class Application {
	
    @Bean
    public Filter userContextFilter() {
        UserContextFilter userContextFilter = new UserContextFilter();
        return userContextFilter;
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}

}
