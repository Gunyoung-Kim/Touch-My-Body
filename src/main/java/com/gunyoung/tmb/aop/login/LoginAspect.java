package com.gunyoung.tmb.aop.login;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.SessionAttributesNotFoundedException;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * 로그인 전 후로 처리해야 하는 aspect
 * @author kimgun-yeong
 *
 */

@Aspect
@Component
@RequiredArgsConstructor
public class LoginAspect {
	
	private final HttpSession session;
	
	/**
	 * 세션에서 로그인된 유저의 ID 가져오는 메소드에서 세션에 유저의 ID 없으면 예외 발생 
	 * @param dp
	 * @author kimgun-yeong
	 */
	@Before("@annotation(com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull)")
	public void checkLoginIdSessionIsNotNull(JoinPoint dp) {
		Long id = SessionUtil.getLoginUserId(session);
		if(id == null) {
			throw new SessionAttributesNotFoundedException(UserErrorCode.SESSION_ATTRIBUTES_NOT_FOUNDED_ERROR.getDescription());
		}
	}
	
}
