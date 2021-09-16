package com.gunyoung.tmb.utils.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.WebAttributes;

import com.gunyoung.tmb.utils.SessionUtil;

/**
* {@link SessionUtil} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) SessionUtil only
* @author kimgun-yeong
*
*/
public class SessionUtilUnitTest {
	
	private HttpSession session;
	
	@BeforeEach
	void setup() {
		session = mock(HttpSession.class);
	}
	
	/*
	 * constructor
	 */
	
	@Test
	@DisplayName("생성자 호출시 예외 발생 확인")
	public void constructorTestThrowsAssertionError() throws NoSuchMethodException {
		//Given
		Constructor<SessionUtil> constructor = SessionUtil.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		
		//When, Then
		assertThrows(InvocationTargetException.class, () -> {
			constructor.newInstance();
		});
	}
	
	/*
	 * public static void setLoginUserId(HttpSession session, Long id)
	 */
	
	@Test
	@DisplayName("로그인한 유저의 id 값(User_id)을 세션에 저장 -> 정상")
	public void setLoginUserIdTest() {
		//Given
		Long loginUserId = Long.valueOf(25);
		
		//When
		SessionUtil.setLoginUserId(session, loginUserId);
		
		//Then
		then(session).should(times(1)).setAttribute(SessionUtil.LOGIN_USER_ID, loginUserId);
	}
	
	/*
	 * public static Long getLoginUserId(HttpSession session)
	 */
	
	@Test
	@DisplayName("세션에서 로그인한 유저의 id 값(User_id)를 반환 -> 정상")
	public void getLoginUserIdTest() {
		//Given
		Long loginUserId = Long.valueOf(24);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginUserId);
		
		//When
		Long result = SessionUtil.getLoginUserId(session);
		
		//Then
		assertEquals(loginUserId, result);
	}
	
	/*
	 * public static void setAfterLoginRedirectedUrl(HttpSession session,String url) 
	 */
	
	@Test
	@DisplayName("로그인 후 리다이렉트 될 주소 세션에 저장 -> 정상")
	public void setAfterLoginRedirectedUrlTest() {
		//Given
		String redirectedURL = "touchmyBody.com";
		
		//When
		SessionUtil.setAfterLoginRedirectedUrl(session, redirectedURL);
		
		//Then
		then(session).should(times(1)).setAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL, redirectedURL);
	}
	
	/*
	 * public static String getAfterLoginRedirectedUrl(HttpSession session)
	 */
	
	@Test
	@DisplayName("로그인 후 리다이렉트 될 주소 반환 -> 정상")
	public void getAfterLoginRedirectedUrlTest() {
		//Given
		String redirectedURL = "touchmyBody.com";
		given(session.getAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL)).willReturn(redirectedURL);
		
		//When
		String result = SessionUtil.getAfterLoginRedirectedUrl(session);
		
		//Then
		assertEquals(redirectedURL, result);
	}
	
	/*
	 * public static void removeAfterLoginRedirectedUrl(HttpSession session) 
	 */
	
	@Test
	@DisplayName("세션에서 로그인 후 리다이렉트 될 주소 삭제 -> 정상")
	public void removeAfterLoginRedirectedUrl() {
		//When
		SessionUtil.removeAfterLoginRedirectedUrl(session);
		
		//Then
		then(session).should(times(1)).removeAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL);
	}
	
	/*
	 * public static void removeAuthenticationExceptionAttributes(HttpSession session)
	 */
	
	@Test
	@DisplayName("로그인 실패 기록 삭제 -> 정상")
	public void removeAuthenticationExceptionAttributesTest() {
		//When
		SessionUtil.removeAuthenticationExceptionAttributes(session);
		
		//Then
		then(session).should(times(1)).removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
	/*
	 * public static void clearSession(HttpSession session) 
	 */
	
	@Test
	@DisplayName("현재 세션에 있는 모든 정보들 삭제 -> 정상")
	public void clearSessionTest() {
		//When
		SessionUtil.clearSession(session);
		
		//Then
		then(session).should(times(1)).invalidate();
	}
	
}
