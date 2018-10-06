package com.jams.authentication.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jams.authentication.model.User;
import com.jams.authentication.model.UserRole;
import com.jams.authentication.repository.UserRepository;
import com.jams.authentication.repository.UserRoleRepository;

@Service
public class JPAUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByName(username);

		if (user == null)
			throw new UsernameNotFoundException("User not found:" + username);
		else {
			List<UserRole> roles = roleRepository.findByUserName(username);
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (UserRole role : roles) {
				authorities.add(new SimpleGrantedAuthority(role.getRole()));
			}
			return new SimpleUserDetails(user.getName(), user.getPassword(),user.getEnabled()==0?false:true , authorities);
		}
	}

	class SimpleUserDetails implements UserDetails {
		private String username;
		private String password;
		private boolean enabled;
		private Collection<? extends GrantedAuthority> authorities;

		public SimpleUserDetails(String username, String password, boolean enabled) {
			super();
			this.username = username;
			this.password = password;
			this.enabled = enabled;
		}

		public SimpleUserDetails(String username, String password, boolean enabled,
				Collection<? extends GrantedAuthority> authorities) {
			super();
			this.username = username;
			this.password = password;
			this.enabled = enabled;
			this.authorities = authorities;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return enabled;
		}
	}
}
