package com.gunyoung.tmb.aop.login.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.aop.login.LoginAspect;
import com.gunyoung.tmb.error.exceptions.nonexist.SessionAttributesNotFoundedException;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link LoginAspect} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) LoginAspect only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class LoginAspectUnitTest {
	
	@Mock
	HttpSession session;
	
	@InjectMocks 
	LoginAspect loginAspect;
	
	/*
	 * void checkLoginIdSessionIsNotNull(JoinPoint dp)
	 */
	
	@Test
	@DisplayName("세션에 로그인 유저 ID 값 없으면 예외 발생 -> 없었다고한다")
	void checkLoginIdSessionIsNotNullNoSuchSession() {
		//Given
		JoinPoint dp = mock(JoinPoint.class);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(null);
		
		//When, Then
		assertThrows(SessionAttributesNotFoundedException.class, () -> {
			loginAspect.checkLoginIdSessionIsNotNull(dp);
		});
	}
	
	@Test
	@DisplayName("세션에 로그인 유저 ID 값 없으면 예외 발생 -> 있었다고 한다.")
	void checkLoginIdSessionIsNotNullTest() {
		//Given
		JoinPoint dp = mock(JoinPoint.class);
		
		Long loginUserId = Long.valueOf(66);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginUserId);
		
		//When
		loginAspect.checkLoginIdSessionIsNotNull(dp);
		
		//Then
		//nothing To do
	}
}
