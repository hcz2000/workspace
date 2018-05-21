package com.jams.oauth.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
    private final String sqlLoadUser = "SELECT user_id,user_name,password FROM t_user WHERE user_name=?";  
    private final String sqlLoadAuthorities="select t2.role_name from t_user_role t1,t_role t2 where t1.role_id=t2.role_id and t1.user_id=?";  
    private final RowMapper<UserDetailsImpl> userDetailsRowMapper;  
    private final RowMapper<GrantedAuthority> authorityRowMapper;  
	
    
    public UserDetailsServiceImpl() {  
        userDetailsRowMapper = new RowMapper<UserDetailsImpl>() {  
            @Override  
            public UserDetailsImpl mapRow(ResultSet rs, int rowNum) throws SQLException {  
                return new UserDetailsImpl(rs.getLong(1), rs.getString(2), rs.getString(3), true);  
            }  
        };  
        authorityRowMapper = new RowMapper<GrantedAuthority>() {  
            @Override  
            public GrantedAuthority mapRow(ResultSet rs, int rowNum)  throws SQLException {  
                return new SimpleGrantedAuthority(rs.getString(1));  
            }  
        };  
    }  
	
	 @Override  
	 public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {  
	        try {  
	            UserDetailsImpl userFromQuery = jdbcTemplate.queryForObject(sqlLoadUser, userDetailsRowMapper, username);  
	            List<GrantedAuthority> authorities = jdbcTemplate.query(sqlLoadAuthorities, authorityRowMapper, userFromQuery.getId());  
	            return new UserDetailsImpl(userFromQuery.getId(), userFromQuery.getUsername(),userFromQuery.getPassword(), userFromQuery.isEnabled(),authorities);  
	        } catch (EmptyResultDataAccessException e) {  
	            throw new UsernameNotFoundException("用户名或密码不正确");  
	        }  
	    }  
	
	
}
