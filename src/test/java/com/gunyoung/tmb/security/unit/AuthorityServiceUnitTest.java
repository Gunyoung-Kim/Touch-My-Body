package com.gunyoung.tmb.security.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.security.AuthorityServiceImpl;

/**
 * {@link AuthorityServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) AuthorityService only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class AuthorityServiceUnitTest {
	
	@Mock
	RoleHierarchy roleHierarchy;
	
	@InjectMocks
	AuthorityServiceImpl authorityService;
	
	/*
	 * List<String> getAuthorityStringsExceptROLE(Collection<? extends GrantedAuthority> authorities)
	 */
	
	@Test
	@DisplayName("Authority들을 ROLE_ 제외한 toString들 반환 -> 정상")
	void getAuthorityStringsExceptROLETest() {
		//Given
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_MANAGER", "ROLE_USER");
		
		//When
		List<String> result = authorityService.getAuthorityStringsExceptROLE(authorities);
		
		//Then
		verifyResult_getAuthorityStringsExceptROLETest(result);
	}
	
	private void verifyResult_getAuthorityStringsExceptROLETest(List<String> result) {
		Set<String> expectedResult = new HashSet<>();
		expectedResult.add("MANAGER");
		expectedResult.add("USER");
		for(String resultString: result) {
			assertTrue(expectedResult.contains(resultString));
		}
	}
	
	/*
	 * Collection<? extends GrantedAuthority> getAuthoritiesByUserRoleType(RoleType roleType)
	 */
	
	@Test
	@DisplayName("유저의 RoleType을 통해 Authority 반환하는 메소드 -> 정상, MANAGER")
	void getAuthoritiesByUserRoleTypeTestManager() {
		//Given
		RoleType role = RoleType.MANAGER;
		
		//When
		Collection<? extends GrantedAuthority> result = authorityService.getAuthoritiesByUserRoleType(role);
		
		//Then
		assertTrue(result.stream().anyMatch((a) -> {
			return a.getAuthority().equals("ROLE_" +role.toString());
		}));
	}
}
