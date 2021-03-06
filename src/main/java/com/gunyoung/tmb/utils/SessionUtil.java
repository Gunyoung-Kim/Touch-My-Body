package com.gunyoung.tmb.utils;

import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;

public class SessionUtil {
	
	public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	public static final String AFTER_LOGIN_REDIRECTED_URL = "AFTER_LOGIN_REDIRECTED_URL";
	
	/**
	 * 인스턴스화 방지
	 * @author kimgun-yeong
	 */
	private SessionUtil() {
		throw new AssertionError();
	}
	
	// ------------------- LOGIN_USER_ID ---------------------------------------
	
	/**
	 * 로그인한 유저의 id 값(User_id)을 세션에 저장하는 메소드 
	 * @param session
	 * @param id
	 * @author kimgun-yeong
	 */
	public static void setLoginUserId(HttpSession session, Long id) {
		session.setAttribute(LOGIN_USER_ID , id);
	}
	
	/**
	 * 세션에서 로그인한 유저의 id 값(User_id)를 반환하는 메소드
	 * @param session
	 * @return
	 */
	public static Long getLoginUserId(HttpSession session) {
		return (Long) session.getAttribute(LOGIN_USER_ID);
	}
	
	// ------------------- AFTER_LOGIN_REDIRECTED_URL  ---------------------------------------
	
	/**
	 * 로그인 후 리다이렉트 될 주소 세션에 저장
	 * @author kimgun-yeong
	 */
	public static void setAfterLoginRedirectedUrl(HttpSession session,String url) {
		session.setAttribute(AFTER_LOGIN_REDIRECTED_URL, url);
	}
	
	/**
	 * 로그인 후 리다이렉트 될 주소 반환 
	 * @author kimgun-yeong
	 */
	public static String getAfterLoginRedirectedUrl(HttpSession session) {
		return (String) session.getAttribute(AFTER_LOGIN_REDIRECTED_URL);
	}
	
	/**
	 * 세션에서 로그인 후 리다이렉트 될 주소 삭제 
	 * @author kimgun-yeong
	 */
	public static void removeAfterLoginRedirectedUrl(HttpSession session) {
		session.removeAttribute(AFTER_LOGIN_REDIRECTED_URL);
	}
	
	// -------------------- AUTHENTICATION ------------------------------------------------
	
	public static void removeAuthenticationExceptionAttributes(HttpSession session) {
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
	
	// -------------------- FOR ALL ------------------------------------------------
	
	/**
	 * 현재 세션에 있는 모든 정보들 삭제
	 * @param session
	 */
	public static void clearSession(HttpSession session) {
		session.invalidate();
	}
	 
}
