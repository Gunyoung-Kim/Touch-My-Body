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

/**
 * 로그인 성공 후 핸들러
 * @author kimgun-yeong
 *
 */
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	public static final String DEFAULT_REDIRECTED_URL_AFTER_LOGIN_SUCCESS = "/";

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
		HttpSession session = request.getSession();
		setUserIdInSessionFromAuthentication(session, authentication);
		String redirectedURL = getRedirectedURLFromRequestAndResponse(request, response);
		
		SessionUtil.removeAfterLoginRedirectedUrl(session);
		SessionUtil.removeAuthenticationExceptionAttributes(session);
		
		response.sendRedirect(redirectedURL);
	}
	
	private void setUserIdInSessionFromAuthentication(HttpSession session, Authentication authentication) {
		String username = authentication.getName();
		Long userId = userService.findByEmail(username).getId();
		SessionUtil.setLoginUserId(session, userId);
	}
	
	private String getRedirectedURLFromRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if(savedRequest != null) {
			String redirectUrlFromSavedRequest = savedRequest.getRedirectUrl();
			requestCache.removeRequest(request, response);
			return redirectUrlFromSavedRequest; 
		}
		
		HttpSession session = request.getSession();
		String redirectedURLFromSession = SessionUtil.getAfterLoginRedirectedUrl(session);
		if(isRedirectedURLFromSessionNonNullAndNonEmpty(redirectedURLFromSession)) {
			return redirectedURLFromSession;
		}
		
		return DEFAULT_REDIRECTED_URL_AFTER_LOGIN_SUCCESS;
	}
	
	private boolean isRedirectedURLFromSessionNonNullAndNonEmpty(String redirectedURLFromSession) {
		return redirectedURLFromSession != null && !redirectedURLFromSession.equals("");
	}
}
