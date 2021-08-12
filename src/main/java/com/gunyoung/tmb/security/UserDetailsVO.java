package com.gunyoung.tmb.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gunyoung.tmb.domain.user.User;

@SuppressWarnings("serial")
public class UserDetailsVO implements UserDetails{
	
	// Spring Security에서 사용하는 username
	private String email;
	
	//password
	private String password;
	
	//유저의 권환 
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsVO() {
		
	}
	
	public UserDetailsVO(User user) {
		email = user.getEmail();
		password = user.getPassword();
		this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
