package com.gunyoung.tmb.controller.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {
	
	private final Environment env;
	
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