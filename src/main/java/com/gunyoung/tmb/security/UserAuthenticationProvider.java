package com.gunyoung.tmb.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider{

	private final UserDetailsService userDetailsService;
	
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * DB 정보를 통해 생성한 UserDetails와 입력된 Authentication을 비교하여 유효성을 검증하고 검증 성공 시 새로운 Authentication 반환 <br>
	 * @throws BadCredentialsException 유효성 검증 실패 시
	 * @author kimgun-yeong
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String inputUsername = authentication.getName();
		String inputPassword = (String) authentication.getCredentials();
		
		UserDetails userDetailsByDB = userDetailsService.loadUserByUsername(inputUsername);
		
		if(!passwordEncoder.matches(inputPassword, userDetailsByDB.getPassword())) {
			throw new BadCredentialsException(inputUsername);
		}
		
		return new UsernamePasswordAuthenticationToken(userDetailsByDB.getUsername(), null, userDetailsByDB.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
