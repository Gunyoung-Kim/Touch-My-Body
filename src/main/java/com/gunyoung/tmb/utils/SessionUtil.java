package com.gunyoung.tmb.utils;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;

public class SessionUtil {
	
	private static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	
	private SessionUtil() {}
	
	/**
	 * 현재 접속자의 username을 SecurityContext에서 가져오는 메소드
	 * @return
	 */
	public static String getUsernameFromSecurityContext() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
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
	
	/**
	 * 세션에서 로그인한 유저의 id 값(User_id)를 삭제하는 메소드
	 * @param session
	 */
	public static void removeLoginUserId(HttpSession session) {
		session.removeAttribute(LOGIN_USER_ID);
	}
	
	/**
	 * 현재 세션에 있는 모든 정보들 삭제
	 * @param session
	 */
	public static void clearSession(HttpSession session) {
		session.invalidate();
	}
	 
}
