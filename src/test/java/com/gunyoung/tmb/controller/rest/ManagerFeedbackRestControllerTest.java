package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.testutil.FeedbackTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * {@link ManagerFeedbackRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerFeedbackRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private FeedbackRepository feedbackRepository;

	/*
	 * @RequestMapping(value="/manager/exercise/feedback/reflect/{feedbackId}", method = RequestMethod.PATCH)
	 * public void reflectFeedback(@PathVariable("feedbackId") Long feedbackId)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("매니저의 피드백 반영 처리 -> 해당 ID의 Feedback 없을 때")
	public void reflectFeedbackNonExist() throws Exception {
		//Given
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		
		feedbackRepository.save(feedback);
		
		//When
		mockMvc.perform(patch("/manager/exercise/feedback/reflect/" + feedback.getId()+1))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(false,feedbackRepository.findById(feedback.getId()).get().isReflected());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("매니저의 피드백 반영 처리 -> 정상")
	public void reflectFeedbackTest() throws Exception {
		//Given
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		
		feedbackRepository.save(feedback);
		
		//When
		mockMvc.perform(patch("/manager/exercise/feedback/reflect/" + feedback.getId()))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(true,feedbackRepository.findById(feedback.getId()).get().isReflected());
	}
}
	