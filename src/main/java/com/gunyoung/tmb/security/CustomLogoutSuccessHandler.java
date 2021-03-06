package com.gunyoung.tmb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.gunyoung.tmb.utils.SessionUtil;

/**
 * 로그 아웃 성공 후 핸들러
 * @author kimgun-yeong
 *
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	public static final String REDIRECRED_URL_AFTER_LOGOUT_SUCCESS = "/";

	/**
	 * 로그아웃 성공시 실행되는 로직 <br>
	 * @author kimgun-yeong
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		SessionUtil.clearSession(session);
		response.sendRedirect(REDIRECRED_URL_AFTER_LOGOUT_SUCCESS);
	}

}
