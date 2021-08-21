package com.gunyoung.tmb.security.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.gunyoung.tmb.security.CustomLogoutSuccessHandler;

/**
 * {@link CustomLogoutSuccessHandler} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) CustomLogoutSuccessHandler only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class CustomLogoutSuccessHandlerUnitTest {
	
	@InjectMocks 
	CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	/*
	 * public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
	 */
	
	@Test
	@DisplayName("로그 아웃 성공 시 처리 로직 -> 정상, 세션 삭제 확인")
	public void onLogoutSuccessTestCheckSessionClear() throws IOException, ServletException {
		//Given
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Authentication authentication = mock(Authentication.class);
		
		given(request.getSession()).willReturn(session);
		
		//When
		customLogoutSuccessHandler.onLogoutSuccess(request, response, authentication);
		
		//Then
		then(session).should(times(1)).invalidate();
	}
	
	@Test
	@DisplayName("로그 아웃 성공 시 처리 로직 -> 정상, 리다이렉트 주소 확인")
	public void onLogoutSuccessTestCheckRedirect() throws IOException, ServletException {
		//Given
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Authentication authentication = mock(Authentication.class);
		
		given(request.getSession()).willReturn(session);
		
		//When
		customLogoutSuccessHandler.onLogoutSuccess(request, response, authentication);
		
		//Then
		then(response).should(times(1)).sendRedirect(CustomLogoutSuccessHandler.REDIRECRED_URL_AFTER_LOGOUT_SUCCESS);
	}
}
