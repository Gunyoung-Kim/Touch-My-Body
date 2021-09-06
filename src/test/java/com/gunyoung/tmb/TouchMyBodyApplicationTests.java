package com.gunyoung.tmb;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Application 테스트
 * @author kimgun-yeong
 *
 */
class TouchMyBodyApplicationTests {
	
	@Test
	@DisplayName("Spring context load 테스트")
	void contextLoads() {
		//Given
		String[] args = {};
		
		//When
		TouchMyBodyApplication.main(args);
		
		//Then
	}
}