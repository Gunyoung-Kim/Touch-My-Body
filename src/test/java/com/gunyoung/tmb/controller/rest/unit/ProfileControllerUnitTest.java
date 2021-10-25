package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import com.gunyoung.tmb.controller.rest.ProfileController;

/**
 * {@link ProfileController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ProfileController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ProfileControllerUnitTest {
	
	@Mock 
	Environment env;
	
	@InjectMocks 
	ProfileController profileController;
	
	/*
	 *  String getProfile()
	 */
	
	@Test
	@DisplayName("프로그램 프로필에서 server를 prefix로 같는 프로필 반환 -> 프로필에 server1 포함")
	void getProfileTest1() {
		//Given
		String server1Profile = "server1";
		String anotherProfile = "another";
		String[] profiles = {server1Profile, anotherProfile};
		given(env.getActiveProfiles()).willReturn(profiles);
		
		//When
		String result = profileController.getProfile();
		
		//Then
		assertEquals(server1Profile, result);
	}
	
	@Test
	@DisplayName("프로그램 프로필에서 server를 prefix로 같는 프로필 반환 -> 그런 프로필 없습니다. 다른 프로필만 있음")
	void getProfileAnother() {
		//Given
		String anotherProfile = "another";
		String[] profiles = { anotherProfile };
		given(env.getActiveProfiles()).willReturn(profiles);
		
		//When
		String result = profileController.getProfile();
		
		//Then
		assertEquals(anotherProfile, result);
	}
	
	@Test
	@DisplayName("프로그램 프로필에서 server를 prefix로 같는 프로필 반환 -> 어떠한 프로필도 없음")
	void getProfileDefault() {
		//Given
		String[] profiles = {};
		given(env.getActiveProfiles()).willReturn(profiles);
		
		//When
		String result = profileController.getProfile();
		
		//Then
		assertEquals(ProfileController.DEFAULT_PROFILE, result);
	}
}
