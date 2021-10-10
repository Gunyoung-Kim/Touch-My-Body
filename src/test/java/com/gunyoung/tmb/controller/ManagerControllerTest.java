package com.gunyoung.tmb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * {@link ManagerController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
class ManagerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 *  @RequestMapping(value="/manager",method = RequestMethod.GET)
     * ModelAndView managerView(ModelAndView mav
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@DisplayName("매니저 메인 화면 반환 -> 정상")
	void managerViewTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/manager"))
		
		//Then
				.andExpect(status().isOk());
	}
	
}
