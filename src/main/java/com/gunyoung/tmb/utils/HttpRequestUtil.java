package com.gunyoung.tmb.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {
	
	/**
	 * HttpRequest의 RemoteHost를 반환하는 메소드 <br>
	 * 리버스 프록시인 Nginx의 사용으로 RemoteHost 값이 127.0.0.1(localhost)로 나타난다. <br>
	 * 기존의 RemoteHost 값은 X-Real-IP 헤더에 담겨져 있기에 이를 반영 <br>
	 * 리버스 프록시가 따로 없다면 기존의 RemoteHost 값 반환
	 * @author kimgun-yeong
	 */
	public static String getRemoteHost(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("X-Real-IP")).orElse(request.getRemoteHost());
	}
}
