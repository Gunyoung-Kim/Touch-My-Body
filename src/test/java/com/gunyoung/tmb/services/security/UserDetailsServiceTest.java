package com.gunyoung.tmb.services.security;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.security.UserDetailsServiceImpl;
import com.gunyoung.tmb.services.domain.user.UserService;

/**
 * {@link UserDetailsServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserDetailsServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
	
	@InjectMocks
	UserDetailsServiceImpl userDetailsService;
	
	@Mock
	UserService userService;
	
	/**
	 *   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	 */
	
	@Test
	@DisplayName("관리자 권한의 UserDetails 반환 -> 정상")
	public void loadUserByUsernameForAdminTest() {
		//Given
		String adminEmail = "admin@test.com";
		User user = getUserWithRole(adminEmail,RoleType.ADMIN);

		given(userService.findByEmail(adminEmail)).willReturn(user);
		
		//When
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		
		//Then
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN")));
	}
	
	@Test
	@DisplayName("매니저 권한의 UserDetials 반환 -> 정상")
	public void loadUserByUsernameForManagerTest() {
		//Given
		String managerEmail = "manager@test.com";
		User user = getUserWithRole(managerEmail,RoleType.MANAGER);
		
		
		given(userService.findByEmail(managerEmail)).willReturn(user);
		
		//When
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		
		//Then
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_MANAGER")));
	}
	
	@Test
	@DisplayName("일반 유저 권한의 UserDetials 반환 -> 정상")
	public void loadUserByUsernameForUserTest() {
		//Given
		String userEmail ="user@test.com";
		User user = getUserWithRole(userEmail,RoleType.USER);
		
		given(userService.findByEmail(userEmail)).willReturn(user);
		
		//When
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		
		//Then
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_USER")));
	}
	
	private User getUserWithRole(String username,RoleType role) {
		User user = User.builder()
				.email(username)
				.password("abcd1234!")
				.role(role)
				.build();
		
		return user;
	}
	
}
