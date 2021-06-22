package com.gunyoung.tmb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		
		String username = authentication.getName();
		
		Long userId = userService.findByEmail(username).getId();
		
		SessionUtil.setLoginUserId(session, userId);
		response.sendRedirect("/");
	}

}
