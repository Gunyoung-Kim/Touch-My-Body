package com.gunyoung.tmb.security.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.security.CustomLoginSuccessHandler;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link CustomLoginSuccessHandler} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) CustomLoginSuccessHandler only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class CustomLoginSuccessHandlerUnitTest {
	
	@Mock
	UserService userService;
	
	@InjectMocks 
	CustomLoginSuccessHandler customLoginSuccessHandler;
	
	private HttpSession session;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private Authentication authentication;
	
	@BeforeEach
	void setup() {
		 session = mock(HttpSession.class);
		 request = mock(HttpServletRequest.class);
		 response = mock(HttpServletResponse.class);
		 authentication = mock(Authentication.class);
	}
	
	/*
	 * public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException 
	 */
	
	@Test
	@DisplayName("Authentication 성공 후 처리 -> 성공 후 리다이렉트 주소 세션에 없는 경우, 해당 내용 세션에서 지우지 않음 확인")
	public void onAuthenticationSuccessTestNoRedirectedURLInSession() throws IOException, ServletException {
		//Given
		String loginUserEmail = UserTest.DEFAULT_EMAIL;
		mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(loginUserEmail);
		
		mockingSessionForGetAfterLoginRedirectedURL(null);
		
		//When
		customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		
		//Then
		then(session).should(never()).removeAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL);
	}
	
	@Test
	@DisplayName("Authentication 성공 후 처리 -> 성공 후 리다이렉트 주소 세션에 있는 경우, 해당 내용 세션에서 지우기 확인 ")
	public void onAuthenticationSuccessTestRedirectedURLExistInSession() throws IOException, ServletException {
		//Given
		String loginUserEmail = UserTest.DEFAULT_EMAIL;
		mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(loginUserEmail);
		
		String redirectedUrlInSession = "/redirected";
		mockingSessionForGetAfterLoginRedirectedURL(redirectedUrlInSession);
		
		//When
		customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		
		//Then
		then(session).should(times(1)).removeAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL);;
	}
	
	@Test
	@DisplayName("Authentication 성공 후 처리 -> SavedRequest not null (spring security에 의해 인터셉트돼서 로그인 페이지로 간 경우)")
	public void onAuthenticationSuccessTestSavedRequestNotNull() throws IOException, ServletException {
		//Given
		String loginUserEmail = UserTest.DEFAULT_EMAIL;
		mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(loginUserEmail);
		
		String redirectedUrlInSession = "/redirected";
		mockingSessionForGetAfterLoginRedirectedURL(redirectedUrlInSession);
		
		mockingRequestGetSessionWithFalseReturnSession();
		
		SavedRequest savedRequest = mock(SavedRequest.class);
		mockingSessionForGetSavedRequest(savedRequest);
		
		//When
		customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		
		//Then
		then(savedRequest).should(times(1)).getRedirectUrl();
	}
	
	@Test
	@DisplayName("Authentication 성공 후 처리 -> SavedRequest null, redirectedUrlFromSession not null and not empty")
	public void onAuthenticationSuccessTestSavedRequestNullAndURLFromSessionNotNullNotEmpty() throws IOException, ServletException {
		//Given
		String loginUserEmail = UserTest.DEFAULT_EMAIL;
		mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(loginUserEmail);
		
		String redirectedUrlInSession = "/redirect";
		mockingSessionForGetAfterLoginRedirectedURL(redirectedUrlInSession);
		
		mockingRequestGetSessionWithFalseReturnSession();
		mockingSessionForGetSavedRequest(null);
		
		//When
		customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		
		//Then
		then(response).should(times(1)).sendRedirect(redirectedUrlInSession);
	}
	
	@Test
	@DisplayName("Authentication 성공 후 처리 -> SavedRequest null, redirectedUrlFromSession not null and empty")
	public void onAuthenticationSuccessTestSavedRequestNullAndURLFromSessionNotNullEmpty() throws IOException, ServletException {
		//Given
		String loginUserEmail = UserTest.DEFAULT_EMAIL;
		mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(loginUserEmail);
		
		String redirectedUrlInSession = "";
		mockingSessionForGetAfterLoginRedirectedURL(redirectedUrlInSession);

		mockingRequestGetSessionWithFalseReturnSession();
		mockingSessionForGetSavedRequest(null);
		
		//When
		customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		
		//Then
		then(response).should(times(1)).sendRedirect("/");
	}
	
	@Test
	@DisplayName("Authentication 성공 후 처리 ->  SavedRequest null, redirectedUrlFromSession null")
	public void onAuthenticationSuccessTestSavedRequestNullAndURLFromSessionNull() throws IOException, ServletException {
		//Given
		String loginUserEmail = UserTest.DEFAULT_EMAIL;
		mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(loginUserEmail);
		mockingSessionForGetAfterLoginRedirectedURL(null);
		mockingRequestGetSessionWithFalseReturnSession();
		mockingSessionForGetSavedRequest(null);
		
		//When
		customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		
		//Then
		then(response).should(times(1)).sendRedirect("/");
	}
	
	@Test
	@DisplayName("Authentication 성공 후 처리 -> 세션에서 로그인 실패 기록 삭제 확인")
	public void onAuthenticationSuccessTestCheckRemoveAuthenticationExceptionAttributes() throws IOException, ServletException {
		//Given
		String loginUserEmail = UserTest.DEFAULT_EMAIL;
		mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(loginUserEmail);
		mockingSessionForGetAfterLoginRedirectedURL(null);
		mockingRequestGetSessionWithFalseReturnSession();
		mockingSessionForGetSavedRequest(null);
		
		//When
		customLoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		
		//Then
		then(session).should(times(1)).removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
	private void mockingReqeustAuthenticationUserServiceForBeforeLoginUserIDInSession(String loginUserEmail) {
		User user = UserTest.getUserInstance(loginUserEmail, RoleType.USER);
		user.setId(Long.valueOf(1));
		
		given(request.getSession()).willReturn(session);
		given(authentication.getName()).willReturn(loginUserEmail);
		given(userService.findByEmail(loginUserEmail)).willReturn(user);
	}
	
	private void mockingSessionForGetAfterLoginRedirectedURL(String redirectedUrlInSession) {
		given(session.getAttribute(SessionUtil.AFTER_LOGIN_REDIRECTED_URL)).willReturn(redirectedUrlInSession);
	}
	
	private void mockingRequestGetSessionWithFalseReturnSession() {
		given(request.getSession(false)).willReturn(session);
	}
	
	private void mockingSessionForGetSavedRequest(SavedRequest savedRequest) {
		String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
		given(session.getAttribute(SAVED_REQUEST)).willReturn(savedRequest);
	}
}
