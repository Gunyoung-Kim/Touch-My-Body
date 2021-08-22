package com.gunyoung.tmb.security.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.security.UserDetailsVO;
import com.gunyoung.tmb.util.UserTest;

/**
 * {@link UserDetailsVO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserDetailsVO only
 * @author kimgun-yeong
 *
 */
public class UserDetailsVOUnitTest {
	
	/*
	 * public UserDetailsVO(User user)
	 */
	
	@Test
	@DisplayName("User 객체를 통한 생성자 -> 정상, Authority확인")
	public void UserDetailsVOByUserTest() {
		//Given
		RoleType userRole = RoleType.MANAGER;
		User user = UserTest.getUserInstance(userRole);
		
		//When
		UserDetailsVO userDetails = new UserDetailsVO(user);
		
		//Then
		assertTrue( userDetails.getAuthorities().stream().anyMatch(a -> {
			return a.getAuthority().equals("ROLE_" + userRole.toString());
		}));
	}
	
	/*
	 * public Collection<? extends GrantedAuthority> getAuthorities()
	 */
	@Test
	@DisplayName("UserDetails에서 Authorities 반환 -> 정상")
	public void getAuthoritiesTest() throws NoSuchFieldException, IllegalAccessException {
		//Given
		UserDetailsVO userDetails = new UserDetailsVO();
		Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + RoleType.USER.toString()));
		Field authoritiesField = userDetails.getClass().getDeclaredField("authorities");
		authoritiesField.setAccessible(true);
		authoritiesField.set(userDetails, authorities);
		
		//When
		Collection<? extends GrantedAuthority> result = userDetails.getAuthorities();
		
		//Then
		assertEquals(authorities, result);
	}
	
	/*
	 * public String getPassword() 
	 */
	@Test
	@DisplayName("UserDetails에서 Password 반환 -> 정상")
	public void getPasswordTest() throws NoSuchFieldException, IllegalAccessException {
		//Given
		UserDetailsVO userDetails = new UserDetailsVO();
		String password = "abcd1234!";
		Field passwordField = userDetails.getClass().getDeclaredField("password");
		passwordField.setAccessible(true);
		passwordField.set(userDetails, password);
		
		//When
		String result = userDetails.getPassword();
		
		//Then
		assertEquals(password, result);
	}
	
	/*
	 * public String getUsername() 
	 */
	@Test
	@DisplayName("UserDetails에서 username 반환 -> 정상")
	public void getUsernameTest() throws NoSuchFieldException, IllegalAccessException {
		//Given
		UserDetailsVO userDetails = new UserDetailsVO();
		String email = "test@test.com";
		Field emailField = userDetails.getClass().getDeclaredField("email");
		emailField.setAccessible(true);
		emailField.set(userDetails, email);
		
		//When
		String result = userDetails.getUsername();
		
		//Then
		assertEquals(email, result);
	}
	
	
	/*
	 * public boolean isAccountNonExpired() 
	 */
	@Test
	@DisplayName("계정 만료 확인 -> 만료되지 않음")
	public void isAccountNonExpiredTrue() {
		//Given
		UserDetailsVO userDetails = new UserDetailsVO();
		
		//When
		boolean result = userDetails.isAccountNonExpired();
		
		//Then
		assertTrue(result);
	}
	
	/*
	 * public boolean isAccountNonLocked() 
	 */
	
	@Test
	@DisplayName("계정 만료 확인 -> 만료되지 않음")
	public void isAccountNonLockedTrue() {
		//Given
		UserDetailsVO userDetails = new UserDetailsVO();
		
		//When
		boolean result = userDetails.isAccountNonLocked();
		
		//Then
		assertTrue(result);
	}
	
	/*
	 * public boolean isCredentialsNonExpired() 
	 */
	
	@Test
	@DisplayName("계정 만료 확인 -> 만료되지 않음")
	public void isCredentialsNonExpiredTrue() {
		//Given
		UserDetailsVO userDetails = new UserDetailsVO();
		
		//When
		boolean result = userDetails.isCredentialsNonExpired();
		
		//Then
		assertTrue(result);
	}
	
	/*
	 * public boolean isEnabled() 
	 */
	
	@Test
	@DisplayName("계정 만료 확인 -> 만료되지 않음")
	public void isEnabledTrue() {
		//Given
		UserDetailsVO userDetails = new UserDetailsVO();
		
		//When
		boolean result = userDetails.isEnabled();
		
		//Then
		assertTrue(result);
	}
}
