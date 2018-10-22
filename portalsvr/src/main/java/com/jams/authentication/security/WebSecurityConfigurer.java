package com.jams.authentication.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	//UserDetailsServie case
	@Autowired
	private JPAUserDetailsService userDetailService;
	
	
	//jdbcAuthentication case
	/*
	@Autowired
	private DataSource dataSource;*/
	
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	@Bean
	@Primary
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//*inMemoryAuthentication case
		/*auth.inMemoryAuthentication().withUser("john.carnell").password("password1").roles("USER").and()
				.withUser("william.woodward").password("password2").roles("USER", "ADMIN");*/
		
		//*jdbcAuthentication case"
		/*auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("select user_name,password,enabled from users where user_name= ?")
			.authoritiesByUsernameQuery("select user_name,role from user_roles where user_name= ?").passwordEncoder(new BCryptPasswordEncoder());*/
		
		//UserDetailsServie case
		auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
	}
	
}