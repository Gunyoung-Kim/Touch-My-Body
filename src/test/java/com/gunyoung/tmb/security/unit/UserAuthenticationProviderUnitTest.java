package com.gunyoung.tmb.security.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.gunyoung.tmb.security.UserAuthenticationProvider;

/**
 * {@link UserAuthenticationProvider} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserAuthenticationProvider only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class UserAuthenticationProviderUnitTest {
	
	@InjectMocks
	UserAuthenticationProvider userAuthenticationProvider;
	
	@Mock
	UserDetailsService userDetailsService;
    
	@Mock
    PasswordEncoder passwordEncoder;
	
	/*
	 * Authentication authenticate(Authentication authentication) throws AuthenticationException 
	 */
	
	@Test
	@DisplayName("DB 정보를 통해 생성한 UserDetails와 입력된 Authentication을 비교하여 유효성을 검증하고 검증 성공 시 새로운 Authentication 반환 -> 비밀 번호 불일치")
	void authenticateTestPasswordNotMatch() throws AuthenticationException {
		//Given
		String inputUserName = "test@test.com";
		String inputPassword = "abcd1234!";
		
		Authentication authentication = mock(Authentication.class);
		given(authentication.getName()).willReturn(inputUserName);
		given(authentication.getCredentials()).willReturn(inputPassword);
		
		String passwordFromDB = "1932cxd3f24d";
		UserDetails userDetailsByDB = mock(UserDetails.class);
		given(userDetailsByDB.getPassword()).willReturn(passwordFromDB);
		
		given(userDetailsService.loadUserByUsername(inputUserName)).willReturn(userDetailsByDB);
		
		given(passwordEncoder.matches(inputPassword, passwordFromDB)).willReturn(false);
		
		//When, Then
		assertThrows(BadCredentialsException.class, () -> {
			userAuthenticationProvider.authenticate(authentication);
		});
	}
	
	@Test
	@DisplayName("DB 정보를 통해 생성한 UserDetails와 입력된 Authentication을 비교하여 유효성을 검증하고 검증 성공 시 새로운 Authentication 반환 -> 정상, Principal 확인")
	void authenticateTestPassworMatchCheckPrincipal() throws AuthenticationException {
		//Given
		String inputUserName = "test@test.com";
		String inputPassword = "abcd1234!";
		
		Authentication authentication = mock(Authentication.class);
		given(authentication.getName()).willReturn(inputUserName);
		given(authentication.getCredentials()).willReturn(inputPassword);
		
		String passwordFromDB = "1932cxd3f24d";
		UserDetails userDetailsByDB = mock(UserDetails.class);
		given(userDetailsByDB.getPassword()).willReturn(passwordFromDB);
		given(userDetailsByDB.getUsername()).willReturn(inputUserName);
		
		given(userDetailsService.loadUserByUsername(inputUserName)).willReturn(userDetailsByDB);
		
		given(passwordEncoder.matches(inputPassword, passwordFromDB)).willReturn(true);
		
		//When
		Authentication result = userAuthenticationProvider.authenticate(authentication);
		
		//Then
		assertEquals(inputUserName, result.getPrincipal());
	}
	
	@Test
	@DisplayName("DB 정보를 통해 생성한 UserDetails와 입력된 Authentication을 비교하여 유효성을 검증하고 검증 성공 시 새로운 Authentication 반환 -> 정상, Credential 확인")
	void authenticateTestPassworMatchCheckCredential() throws AuthenticationException {
		//Given
		String inputUserName = "test@test.com";
		String inputPassword = "abcd1234!";
		
		Authentication authentication = mock(Authentication.class);
		given(authentication.getName()).willReturn(inputUserName);
		given(authentication.getCredentials()).willReturn(inputPassword);
		
		String passwordFromDB = "1932cxd3f24d";
		UserDetails userDetailsByDB = mock(UserDetails.class);
		given(userDetailsByDB.getPassword()).willReturn(passwordFromDB);
		
		given(userDetailsService.loadUserByUsername(inputUserName)).willReturn(userDetailsByDB);
		
		given(passwordEncoder.matches(inputPassword, passwordFromDB)).willReturn(true);
		
		//When
		Authentication result = userAuthenticationProvider.authenticate(authentication);
		
		//Then
		assertEquals(null, result.getCredentials());
	}
	
	/*
	 * boolean supports(Class<?> authentication)
	 */
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> AbstractAuthenticationToken")
	void supportsTestAbstractAuthenticationToken() {
		//Given
		Class<?> authentication = AbstractAuthenticationToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);
		
		//Then
		assertFalse(result);
	}
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> AnonymousAuthenticationToken")
	void supportsTestAnonymousAuthenticationToken() {
		//Given
		Class<?> authentication = AnonymousAuthenticationToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);
		
		//Then
		assertFalse(result);
	}
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> CasAssertionAuthenticationToken")
	void supportsTestCasAssertionAuthenticationToken() {
		//Given
		Class<?> authentication = AbstractAuthenticationToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);
		
		//Then
		assertFalse(result);
	}
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> PreAuthenticatedAuthenticationToken")
	void supportsTestPreAuthenticatedAuthenticationTokenn() {
		//Given
		Class<?> authentication = PreAuthenticatedAuthenticationToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);
		
		//Then
		assertFalse(result);
	}
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> RememberMeAuthenticationToken")
	void supportsTestRememberMeAuthenticationToken() {
		//Given
		Class<?> authentication = RememberMeAuthenticationToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);
		
		//Then
		assertFalse(result);
	}
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> RunAsUserToken")
	void supportsTestRunAsUserToken() {
		//Given
		Class<?> authentication = RunAsUserToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);
		
		//Then
		assertFalse(result);
	}
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> TestingAuthenticationToken")
	void supportsTestTestingAuthenticationToken() {
		//Given
		Class<?> authentication = TestingAuthenticationToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);
		
		//Then
		assertFalse(result);
	}
	
	@Test
	@DisplayName("AuthenticationProvider 가 주어진 Authentication을 처리할 수 있는지 여부 -> UsernamePasswordAuthenticationToken")
	void supportsTestUsernamePasswordAuthenticationToken() {
		//Given
		Class<?> authentication = UsernamePasswordAuthenticationToken.class;
		
		//When
		boolean result = userAuthenticationProvider.supports(authentication);

		//Then
		assertTrue(result);
	}
}
