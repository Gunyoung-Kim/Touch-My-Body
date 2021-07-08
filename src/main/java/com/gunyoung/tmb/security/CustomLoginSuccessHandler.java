package com.gunyoung.tmb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final UserService userService;
	
	/**
	 * 로그인 성공 시 실행되는 로직 <br>
	 * 세션에 로그인 유저의 ID 값 저장 <br>
	 * spring security에 의해 인터셉트돼서 로그인 페이지로 간 경우 -> SavedRequest 객체 통해 원래 가려던 페이지로 로그인 성공 후 이동
	 * 
	 * @author kimgun-yeong
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		String redirectedUrl ="/";
		HttpSession session = request.getSession();
		
		/*
		 *  세션에 로그인 유저의 ID 값 저장
		 */
		String username = authentication.getName();
		
		Long userId = userService.findByEmail(username).getId();
		
		SessionUtil.setLoginUserId(session, userId);
		
		/*
		 *  로그인 성공 이후 리다이렉트 될 주소 구하는 로직
		 */
		
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		String redirectedUrlFromSession = SessionUtil.getAfterLoginRedirectedUrl(session);
		
		if(redirectedUrlFromSession != null) {
			SessionUtil.removeAfterLoginRedirectedUrl(session);
		}
		
		if(savedRequest != null) {
			redirectedUrl = savedRequest.getRedirectUrl();
			requestCache.removeRequest(request, response);
		} else if(redirectedUrlFromSession != null && !redirectedUrlFromSession.equals("")) {
			redirectedUrl = redirectedUrlFromSession;
		}
		
		
		// 세션에서 로그인 실패 기록 삭제 
		SessionUtil.removeAuthenticationExceptionAttributes(session);
		
		response.sendRedirect(redirectedUrl);
	}
	

	
}
