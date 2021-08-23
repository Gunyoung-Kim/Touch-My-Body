package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.ManagerFeedbackController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.FeedbackTest;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * {@link ManagerFeedbackController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerFeedbackController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ManagerFeedbackControllerUnitTest {
	
	@Mock
	FeedbackService feedbackService;
	
	@Mock
	ExerciseService exerciseService;
	
	@InjectMocks
	ManagerFeedbackController managerFeedbackController;
	
	private ModelAndView mav;
	private ModelAndPageView mapv;
	
	private int defaultPageNum = 1;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		mapv = mock(ModelAndPageView.class);
	}
	
	/*
	 * public ModelAndView feedbackListView(@PathVariable("exerciseId") Long exerciseId,@RequestParam(value="page", defaultValue="1") int page
	 *		,ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("특정 Exercise 정보에 대해 작성된 Feedback 리스트 화면 반환 -> 해당 Id의 Exercise 없으면")
	public void feedbackListViewTestExerciseNonExist() {
		//Given
		Long nonExistExerciseId = Long.valueOf(1);
		given(exerciseService.findById(nonExistExerciseId)).willReturn(null);
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			managerFeedbackController.feedbackListView(nonExistExerciseId, 1, mapv);
		});
	}
	
	@Test
	@DisplayName("특정 Exercise 정보에 대해 작성된 Feedback 리스트 화면 반환 -> FeedbackService 체크")
	public void feedbackListViewTestCheckFeedbackService() {
		//Given
		Long exerciseId = Long.valueOf(1);
		stubbingExerciseServiceFindById(exerciseId);
		
		//When
		managerFeedbackController.feedbackListView(exerciseId, defaultPageNum, mapv);
		
		//Then
		then(feedbackService).should(times(1)).findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, defaultPageNum, PageUtil.FEEDBACK_FOR_MANAGE_PAGE_SIZE);
		then(feedbackService).should(times(1)).countByExerciseId(exerciseId);
	}
	
	@Test
	@DisplayName("특정 Exercise 정보에 대해 작성된 Feedback 리스트 화면 반환 -> ModelAndPageView 체크")
	public void feedbackListViewTestCheckViewName() {
		//Given
		Long exerciseId = Long.valueOf(1);
		Exercise exercise = stubbingExerciseServiceFindById(exerciseId);
		
		Page<FeedbackManageListDTO> pageResult = new PageImpl<>(new ArrayList<>());
		given(feedbackService.findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, defaultPageNum, PageUtil.FEEDBACK_FOR_MANAGE_PAGE_SIZE)).willReturn(pageResult);
		Long givenFeedbackNum = Long.valueOf(10);
		given(feedbackService.countByExerciseId(exerciseId)).willReturn(givenFeedbackNum);
		
		//When
		managerFeedbackController.feedbackListView(exerciseId, defaultPageNum, mapv);
		
		//Then
		verfiyMapvInFeedbackListViewTestCheckViewName(pageResult, exercise, givenFeedbackNum);
	}
	
	private void verfiyMapvInFeedbackListViewTestCheckViewName(Page<FeedbackManageListDTO> pageResult, Exercise exercise, Long givenFeedbackNum) {
		then(mapv).should(times(1)).addObject("listObject", pageResult);
		then(mapv).should(times(1)).addObject("exerciseName", exercise.getName());
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenFeedbackNum/ PageUtil.FEEDBACK_FOR_MANAGE_PAGE_SIZE +1);
		then(mapv).should(times(1)).setViewName("feedbackListViewForManage");
	}
	
	private Exercise stubbingExerciseServiceFindById(Long exerciseId) {
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exercise.setId(exerciseId);
		given(exerciseService.findById(exerciseId)).willReturn(exercise);
		return exercise;
	}
	
	/*
	 * public ModelAndView feedbackView(@PathVariable("feedbackId") Long feedbackId, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("특정 Feedback의 상세정보 화면 반환 -> 해당 Id의 Feedback 없으면")
	public void feedbackViewFeedbackNonExist() {
		//Given
		Long nonExistFeedbackId = Long.valueOf(1);
		given(feedbackService.findForFeedbackViewDTOById(nonExistFeedbackId)).willReturn(null);
		
		//When, Then
		assertThrows(FeedbackNotFoundedException.class, () -> {
			managerFeedbackController.feedbackView(nonExistFeedbackId, mav);
		});
	}
	
	@Test
	@DisplayName("특정 Feedback의 상세정보 화면 반환 -> 정상, ModelAndView 체크")
	public void feedbackViewTestModelAndView() {
		//Given
		Long feedbackId = Long.valueOf(1);
		FeedbackViewDTO feedbackViewDTO = FeedbackTest.getFeedbackViewDTOInstance();
		given(feedbackService.findForFeedbackViewDTOById(feedbackId)).willReturn(feedbackViewDTO);
		
		//When
		managerFeedbackController.feedbackView(feedbackId, mav);
		
		//Then
		verifyMavInFeedbackViewTestModelAndView(feedbackViewDTO);
	}
	
	private void verifyMavInFeedbackViewTestModelAndView(FeedbackViewDTO feedbackViewDTO) {
		then(mav).should(times(1)).addObject("feedbackInfo", feedbackViewDTO);
		then(mav).should(times(1)).setViewName("feedbackView");
	}
}
