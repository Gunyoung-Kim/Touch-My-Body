package com.gunyoung.tmb.services.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.security.AuthorityServiceImpl;

/**
 * {@link AuthorityServiceImpl}에 대한 테스트 클래스 <br> 
 * 테스트 범위: (단위 테스트) Service only <br>
 * {@link org.springframework.security.core.context.SecurityContext} 사용을 위해 SpringExtension 활용
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(SpringExtension.class)
public class AuthorityServiceTest {
	
	@Mock
	RoleHierarchy roleHierarchy;
	
	@InjectMocks
	AuthorityServiceImpl authorityService;
	
	/*
	 * public boolean isSessionUserAuthorityCanAccessToTargetAuthority(User target)
	 */
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@DisplayName("세션 접속자의 권한으로 인자로 전달된 유저의 권한에 접근 가능한지 여부 반환 -> 서로 동일한 권한일때")
	public void isSessionUserAuthorityCanAccessToTargetAuthoritySameTest() {
		//Given
		User targetUser = getUserInstance(RoleType.MANAGER);
		
		given(roleHierarchy.getReachableGrantedAuthorities(any())).willAnswer(new Answer<Collection<? extends GrantedAuthority>>() {
			private int count = 0;
			
			@Override
			public Collection<? extends GrantedAuthority> answer(InvocationOnMock invocation) throws Throwable {
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				if(count == 0) {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.MANAGER.toString()));
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.USER.toString()));
					count++;
				} else {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.MANAGER.toString()));
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.USER.toString()));
				}
				
				return authorities;
			}
			
		});
		
		//When
		boolean result = authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(targetUser);
		
		//Then
		assertTrue(result);
	}
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@DisplayName("세션 접속자의 권한으로 인자로 전달된 유저의 권한에 접근 가능한지 여부 반환 -> 세션 접속자가 더 높을 때")
	public void isSessionUserAuthorityCanAccessToTargetAuthoritySessionUserHigherTest() {
		//Given
		User targetUser = getUserInstance(RoleType.USER);
		
		given(roleHierarchy.getReachableGrantedAuthorities(any())).willAnswer(new Answer<Collection<? extends GrantedAuthority>>() {
			private int count = 0;
			
			@Override
			public Collection<? extends GrantedAuthority> answer(InvocationOnMock invocation) throws Throwable {
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				if(count == 0) {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.MANAGER.toString()));
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.USER.toString()));
					count++;
				} else {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.USER.toString()));
				}
				
				return authorities;
			}
			
		});
		
		//When
		boolean result = authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(targetUser);
		
		//Then
		assertTrue(result);
	}
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@DisplayName("세션 접속자의 권한으로 인자로 전달된 유저의 권한에 접근 가능한지 여부 반환 -> 인자로 전달된 유저가 더 높을 때")
	public void isSessionUserAuthorityCanAccessToTargetAuthorityTargetHigherTest() {
		//Given
		User targetUser = getUserInstance(RoleType.ADMIN);
		
		given(roleHierarchy.getReachableGrantedAuthorities(any())).willAnswer(new Answer<Collection<? extends GrantedAuthority>>() {
			private int count = 0;
			
			@Override
			public Collection<? extends GrantedAuthority> answer(InvocationOnMock invocation) throws Throwable {
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				if(count == 0) {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.MANAGER.toString()));
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.USER.toString()));
					count++;
				} else {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.ADMIN.toString()));
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.MANAGER.toString()));
					authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.USER.toString()));
				}
				
				return authorities;
			}
			
		});
		
		//When
		boolean result = authorityService.isSessionUserAuthorityCanAccessToTargetAuthority(targetUser);
		
		//Then
		assertFalse(result);
	}
	
	private User getUserInstance(RoleType role) {
		User user = User.builder()
				.email("test@test.com")
				.firstName("first")
				.lastName("last")
				.role(role)
				.build();
		
		return user;
	}
	
	
	/*
	 * public List<String> getAuthorityStringsExceptROLE(Collection<? extends GrantedAuthority> authorities) 
	 */
	
	@Test
	@DisplayName("Authority들을 ROLE_ 제외한 toString들 반환 -> 정상")
	public void getAuthorityStringsExceptROLETest() {
		//Given
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(RoleType role: RoleType.values()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
		}
		
		//When
		List<String> result = authorityService.getAuthorityStringsExceptROLE(authorities);
		
		//Then
		
		for(String authorityString: result) {
			assertFalse(authorityString.startsWith("ROLE_"));
		}
	}
	
	/*
	 * public Collection<? extends GrantedAuthority> getAuthoritiesByUserRoleType(RoleType roleType)
	 */
	
	@Test
	@DisplayName("User의 RoleType을 통해 Authority 반환 -> RoleTyp.USER")
	public void getAuthoritiesByUserRoleTypeUSERTest() {
		//Given
		RoleType userType = RoleType.USER;
		
		//When
		Collection<? extends GrantedAuthority> result = authorityService.getAuthoritiesByUserRoleType(userType);
		
		//Then
		Set<String> authorities = new HashSet<>();
		result.forEach((authority) -> {
			authorities.add(authority.getAuthority());
		});
		
		assertEquals(true, authorities.contains("ROLE_USER"));
	}
	
	@Test
	@DisplayName("Manager의 RoleType을 통해 Authority 반환 -> RoleTyp.MANAGER")
	public void getAuthoritiesByUserRoleTypeMANAGERTest() {
		//Given
		RoleType userType = RoleType.MANAGER;
		
		//When
		Collection<? extends GrantedAuthority> result = authorityService.getAuthoritiesByUserRoleType(userType);
		
		//Then
		Set<String> authorities = new HashSet<>();
		result.forEach((authority) -> {
			authorities.add(authority.getAuthority());
		});
		
		assertEquals(true, authorities.contains("ROLE_MANAGER"));
	}
	
	@Test
	@DisplayName("Administrator의 RoleType을 통해 Authority 반환 -> RoleTyp.ADMIN")
	public void getAuthoritiesByUserRoleTypeADMINTest() {
		//Given
		RoleType userType = RoleType.ADMIN;
		
		//When
		Collection<? extends GrantedAuthority> result = authorityService.getAuthoritiesByUserRoleType(userType);
		
		//Then
		Set<String> authorities = new HashSet<>();
		result.forEach((authority) -> {
			authorities.add(authority.getAuthority());
		});
		
		assertEquals(true, authorities.contains("ROLE_ADMIN"));
	}
}
