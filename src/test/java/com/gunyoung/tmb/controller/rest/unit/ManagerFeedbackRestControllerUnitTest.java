package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.ManagerFeedbackRestController;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.util.FeedbackTest;

/**
 * {@link ManagerFeedbackRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerFeedbackRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ManagerFeedbackRestControllerUnitTest {
	
	@Mock
	FeedbackService feedbackService;
	
	@InjectMocks
	ManagerFeedbackRestController managerFeedbackRestController;
	
	/*
	 * public void reflectFeedback(@PathVariable("feedbackId") Long feedbackId)
	 */
	
	@Test
	@DisplayName("매니저의 특정 Feedback 반영 요청 처리 -> 해당 Id의 Feedback 없으면")
	public void reflectFeedbackFeedbackNonExist() {
		//Given
		Long nonExistFeedbackId = Long.valueOf(1);
		given(feedbackService.findById(nonExistFeedbackId)).willReturn(null);
		
		//When, Then
		assertThrows(FeedbackNotFoundedException.class, () -> {
			managerFeedbackRestController.reflectFeedback(nonExistFeedbackId);
		});
	}
	
	@Test
	@DisplayName("매니저의 특정 Feedback 반영 요청 처리 -> 정상, 반영 확인")
	public void reflectFeedbackTestCheckReflected() {
		//Given
		Long feedbackId = Long.valueOf(1);
		Feedback feedback = stubbingFeedbackServiceFindById(feedbackId);
		
		//When
		managerFeedbackRestController.reflectFeedback(feedbackId);
		
		//Then
		assertTrue(feedback.isReflected());
	}
	
	@Test
	@DisplayName("매니저의 특정 Feedback 반영 요청 처리 -> 정상, 저장 확인")
	public void reflectFeedbackTestCheckSave() {
		//Given
		Long feedbackId = Long.valueOf(1);
		Feedback feedback = stubbingFeedbackServiceFindById(feedbackId);
		
		//When
		managerFeedbackRestController.reflectFeedback(feedbackId);
		
		//Then
		then(feedbackService).should(times(1)).save(feedback);
	}
	
	private Feedback stubbingFeedbackServiceFindById(Long feedbackId) {
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		given(feedbackService.findById(feedbackId)).willReturn(feedback);
		return feedback;
	}
}
