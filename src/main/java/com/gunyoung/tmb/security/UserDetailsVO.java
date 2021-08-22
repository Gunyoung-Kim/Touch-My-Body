package com.gunyoung.tmb.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gunyoung.tmb.domain.user.User;

import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
public class UserDetailsVO implements UserDetails{
	
	// Spring Security에서 사용하는 username
	private String email;
	
	//password
	private String password;
	
	//유저의 권환 
	private Collection<? extends GrantedAuthority> authorities;
	
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
	
	/**
	 * 아직까진 계정 만료 정책이 따로 있지 않음
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 계정 잠금 정책 없음
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 비밀번호 만료 정책 없음
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
