package com.gunyoung.tmb.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {
	
	/**
	 * HTTP Request의 remote host 반환 <br>
	 * nginx를 리버스 프록시 서버로 사용하는 경우 원하는 값이 X-Real-IP 헤더에 포함되기에 이를 반영
	 * @param request
	 * @author kimgun-yeong
	 */
	public static String getRemoteHost(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("X-Real-IP")).orElse(request.getRemoteHost());
	}
}
