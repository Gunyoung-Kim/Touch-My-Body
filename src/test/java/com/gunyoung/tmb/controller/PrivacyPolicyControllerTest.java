package com.gunyoung.tmb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * {@link PrivacyPolicyController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PrivacyPolicyControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 * @RequestMapping(value="/privacypolicy", method = RequestMethod.GET)
	 * public ModelAndView privacyPolicyLastest(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("가장 최근 개인정보 처리방침 화면 반환 -> 정상")
	public void privacyPolicyLastestViewTest() throws Exception{
		//Given
		int lastestVersion = PrivacyPolicyController.LATEST_POLICY_VERSION;
		
		//When
		mockMvc.perform(get("/privacypolicy"))
		
		//Then
				.andExpect(view().name("privacyPolicy_" + lastestVersion));
	}
	
	/*
	 * @RequestMapping(value="/privacypolicy/{version}", method = RequestMethod.GET)
	 * public ModelAndView privacyPolicyWithVersion(@PathVariable("version") int version,ModelAndView mav
	 */
	
	@Test
	@DisplayName("특정 버전의 개인정보 처리방침 화면 반환 -> 올바르지 못한 버전")
	public void privacyPolicyWithVersionInValidVersion() throws Exception {
		//Given
		int invalidVersion = PrivacyPolicyController.LATEST_POLICY_VERSION + 10;
		
		//When
		mockMvc.perform(get("/privacypolicy/" + invalidVersion))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("특정 버전의 개인정보 처리방침 화면 반환 -> 정상")
	public void privacyPolicyWithVersionValidVersion() throws Exception {
		//Given
		int version = PrivacyPolicyController.LATEST_POLICY_VERSION;
		
		//When
		mockMvc.perform(get("/privacypolicy/" + version))
		
		//Then
				.andExpect(view().name("privacyPolicy_" + version));
	}
}
