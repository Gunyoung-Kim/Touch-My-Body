package com.gunyoung.tmb.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.services.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	private final UserService userService;
	
	/**
	 * @param username 본 어플리케이션에선 사용자의 email을 username으로 활용한다.
	 * @return UserDetails
	 * @author kimgun-yeong
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByEmail(username);
		if(user ==null) 
			throw new UsernameNotFoundException("User not found with: " + username);
		UserDetails userDetails = new UserDetailsVO(user);
		
		return userDetails;
	}

}
