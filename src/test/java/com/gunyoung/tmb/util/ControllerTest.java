package com.gunyoung.tmb.util;

import java.util.Map;

import org.springframework.test.web.servlet.MvcResult;

/**
 * Controller 클래스들의 테스트 코드에서 사용하는 유틸리티 메소드 모아놓은 클래스
 * @author kimgun-yeong
 *
 */
public class ControllerTest {
	
	/**
	 * MvcResult에서 Model 반환
	 * @author kimgun-yeong
	 */
	public static Map<String, Object> getResponseModel(MvcResult mvcResult) {
		return mvcResult.getModelAndView().getModel();
	}
}
