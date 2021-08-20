package com.gunyoung.tmb.aop.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gunyoung.tmb.utils.HttpRequestUtil;

/**
 *  로깅을 위한 클래스 
 * @author kimgun-yeong
 *
 */
@Component
@Aspect
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	/**
	 * 컨트롤러들의 포인트컷
	 * @author kimgun-yeong
	 */
	@Pointcut("within(com.gunyoung.tmb.controller..*)")
	public void onRequest() {}
	
	/**
	 * 컨트롤러에 Request 가 들어오면 Request 메소드,uri, parameters, remote address, 처리 시간 들을 로깅 하기 위한 어드바이스
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 * @author kimgun-yeong
	 */
	@Around("com.gunyoung.tmb.aop.log.LogAspect.onRequest()")
	public Object loggingAroundController(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		Map<String, String[]> paramMap = request.getParameterMap();
		String params = "";
		if(paramMap.isEmpty() == false) {
			params = "[ " + paramMapToString(paramMap) + "]";
		}
		
		long timeBeforeProceed = System.currentTimeMillis();
		try {
			return pjp.proceed(pjp.getArgs());
		} finally {
			long timeAfterProceed = System.currentTimeMillis();
			logger.info("Request: {} {}{} < {} ({}ms)",request.getMethod(), request.getRequestURI(), params, 
					HttpRequestUtil.getRemoteHost(request), timeAfterProceed- timeBeforeProceed);
		}
	}
	
	/**
	 * Request Parameter 들을 로깅을 위해 한줄로 변환하는 메소드 
	 * @param paramMap
	 * @return Request Parameter 한줄
	 * @author kimgun-yeong
	 */
	private String paramMapToString(Map<String, String[]> paramMap) {
		StringBuilder sb = new StringBuilder();
		
		paramMap.entrySet().stream().forEach(entry -> {
			sb.append(entry.getKey() + " : ");
			for(int i=0 ; i < entry.getValue().length ; i++) {
				sb.append(entry.getValue()[i] +" ");
			}
			sb.append(", ");
		});
		
		String paramString = sb.toString();
		paramString = paramString.substring(0, paramString.length()-2);
		
		return paramString;
	}
}
