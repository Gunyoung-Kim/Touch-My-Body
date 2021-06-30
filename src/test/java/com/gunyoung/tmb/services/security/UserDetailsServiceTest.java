package com.gunyoung.tmb.services.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.security.UserDetailsServiceImpl;
import com.gunyoung.tmb.services.domain.user.UserService;

/**
 * UserDetailsService 빈 테스트 클래스
 * 테스트 범위: UserDetailsServiceImpl 단 하나의 레이어
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserDetailsServiceTest {
	
	UserDetailsServiceImpl userDetailsService;
	
	UserService userServiceMock;
	
	@BeforeEach
	void setup() {
		userServiceMock = mock(UserService.class);
		userDetailsService = new UserDetailsServiceImpl(userServiceMock);
	}
	
	@AfterEach
	void tearDown() {
		
	}
	
	/**
	 *   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	 */
	
	@Test
	@DisplayName("관리자 권한의 UserDetails 반환 -> 정상")
	public void loadUserByUsernameForAdminTest() {
		//Given
		String adminEmail = "admin@test.com";
		User user = getUserWithRole(adminEmail,RoleType.ADMIN);

		when(userServiceMock.findByEmail(adminEmail)).thenReturn(user);
		
		//When
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		
		//Then
		assertEquals(userDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN")),true);
	}
	
	@Test
	@DisplayName("매니저 권한의 UserDetials 반환 -> 정상")
	public void loadUserByUsernameForManagerTest() {
		//Given
		String managerEmail = "manager@test.com";
		User user = getUserWithRole(managerEmail,RoleType.MANAGER);
		
		
		when(userServiceMock.findByEmail(managerEmail)).thenReturn(user);
		
		//When
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		
		//Then
		assertEquals(userDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_MANAGER")),true);
	}
	
	@Test
	@DisplayName("일반 유저 권한의 UserDetials 반환 -> 정상")
	public void loadUserByUsernameForUserTest() {
		//Given
		String userEmail ="user@test.com";
		User user = getUserWithRole(userEmail,RoleType.USER);
		
		when(userServiceMock.findByEmail(userEmail)).thenReturn(user);
		
		//When
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		
		//Then
		assertEquals(userDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_USER")),true);
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
