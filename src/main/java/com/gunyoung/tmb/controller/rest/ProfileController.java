package com.gunyoung.tmb.controller.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Code의 무중단 배포를 위한 프로필 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ProfileController {
	
	private final Environment env;
	
	/**
	 * 적용된 프로필중 server1 이나 server2 있으면 반환
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/profile", method= RequestMethod.GET)
	public String getProfile() {
		List<String> profiles = Arrays.asList(env.getActiveProfiles());
		List<String> forNonStopProfiles = Arrays.asList("server1","server2");
		
		String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
		
		return Arrays.stream(env.getActiveProfiles())
				.filter(forNonStopProfiles::contains)
				.findAny()
				.orElse(defaultProfile);
	}
	
}