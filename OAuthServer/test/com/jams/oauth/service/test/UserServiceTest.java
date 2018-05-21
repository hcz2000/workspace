package com.jams.oauth.service.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/config/spring-context.xml"})
public class UserServiceTest {
	
	@Autowired
	private UserDetailsService userService;

	
	@Test
	public void findUserByUserName(){
		UserDetails user=userService.loadUserByUsername("client-0001");
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		assertEquals(user.getUsername(),"client-0001");
		assertEquals(user.getAuthorities().toString(),"[ROLE_CLIENT]");
	}
	
}
