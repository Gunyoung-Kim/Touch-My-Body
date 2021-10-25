package com.gunyoung.tmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.error.codes.PrivacyPolicyErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.PrivacyPolicyNotFoundedException;

/**
 * 개인정보 처리방침 화면 반환하는 메소드
 * @author kimgun-yeong
 *
 */
@Controller
public class PrivacyPolicyController {
	
	public static final int LATEST_POLICY_VERSION = 1;
	
	/**
	 *  가장 최근에 적용된 개인정보 처리방침 문서 화면 반환하는 메소드
	 *  @author kimgun-yeong
	 */
	@GetMapping(value = "/privacypolicy")
	public ModelAndView privacyPolicyLastest(ModelAndView mav) {
		mav.setViewName("privacyPolicy_" + LATEST_POLICY_VERSION);
		
		return mav;
	}
	
	/**
	 *  특정 버전의 개인정보 처리방침 화면 반환하는 메소드
	 *  @param version 열람하려는 개인정보 처리방침의 버전 값 
	 *  @throws PrivacyPolicyNotFoundedException 해당 버전의 개인정보 처리방침 없으면
	 *  @author kimgun-yeong
	 */
	@GetMapping(value = "/privacypolicy/{version}")
	public ModelAndView privacyPolicyWithVersion(@PathVariable("version") int version, ModelAndView mav) {
		if(version <= 0 || version > LATEST_POLICY_VERSION) {
			throw new PrivacyPolicyNotFoundedException(PrivacyPolicyErrorCode.PRIVACY_NOT_FOUNDED_ERROR.getDescription());
		}
		mav.setViewName("privacyPolicy_" +version);
		
		return mav;
	}
}
