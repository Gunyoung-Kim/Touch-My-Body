package com.gunyoung.tmb.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {
	
	public static String getRemoteHost(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("X-Real-IP")).orElse(request.getRemoteHost());
	}
}
