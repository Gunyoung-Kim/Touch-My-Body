package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.FeedbackController;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.SaveFeedbackDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.FeedbackTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link FeedbackController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) FeedbackController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class FeedbackControllerUnitTest {
	
	@Mock
	FeedbackService feedbackService;
	
	@Mock
	UserService userService;
	
	@Mock
	ExerciseService exerciseService;
	
	@Mock
	HttpSession session;
	
	@InjectMocks 
	FeedbackController feedbackController;
	
	private ModelAndView mav;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		saveFeedbackDTO = FeedbackTest.getSaveFeedbackDTOInstance();
	}
	
	/*
	 * ModelAndView addFeedbackView(@PathVariable("exercise_id") Long exerciseId,ModelAndView mav)
	 */
	
	@Test
	@DisplayName("Feedback 추가 화면 반환 -> 해당 ID의 Exercise 없으면")
	void addFeedbackViewExerciseNonExist() {
		//Given
		Long nonExistExerciseId = Long.valueOf(66);
		given(exerciseService.findById(nonExistExerciseId)).willThrow(new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			feedbackController.addFeedbackView(nonExistExerciseId, mav);
		});
	}
	
	@Test
	@DisplayName("Feedback 추가 화면 반환 -> 정상, ModelAndView Check")
	void addFeedbackViewTestCheckMav() {
		//Given
		Long exerciseId = Long.valueOf(36);
		Exercise exercise = ExerciseTest.getExerciseInstance();
		given(exerciseService.findById(exerciseId)).willReturn(exercise);
		
		//When
		feedbackController.addFeedbackView(exerciseId, mav);
		
		//Then
		thenModelAndViewShouldInAddFeedbackViewTestCheckMav(exercise);
	}
	
	private void thenModelAndViewShouldInAddFeedbackViewTestCheckMav(Exercise exercise) {
		then(mav).should(times(1)).addObject("exerciseName", exercise.getName());
		then(mav).should(times(1)).setViewName("addFeedback");
	}
	
	/*
	 * ModelAndView addFeedback(@PathVariable("exercise_id") Long exerciseId, @ModelAttribute SaveFeedbackDTO dto)
	 */
	
	private SaveFeedbackDTO saveFeedbackDTO;
	
	@Test
	@DisplayName("Feedback 추가 처리 -> 세션에 저장된 ID에 해당하는 User 없으면")
	void addFeedbackTestUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(73);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findWithFeedbacksById(nonExistUserId)).willThrow(new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription()));
		
		Long exerciseId = Long.valueOf(34);
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			feedbackController.addFeedback(exerciseId, saveFeedbackDTO);
		});
	}
	
	@Test
	@DisplayName("Feedback 추가 처리 -> 해당 ID에 해당하는 Exercise 없으면")
	void addFeedbackExerciseNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(92);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		stubbingUserServiceFindWithFeedbacksById(loginIdInSession);
		
		Long nonExistExerciseId = Long.valueOf(152);
		given(exerciseService.findWithFeedbacksById(nonExistExerciseId)).willThrow(new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			feedbackController.addFeedback(nonExistExerciseId, saveFeedbackDTO);
		});
	}
	
	@Test
	@DisplayName("Feedback 추가 처리 -> 정상, feedbackService check")
	void addFeedbackTestCheckFeedbackService() {
		//Given
		Long loginIdInSession = Long.valueOf(92);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		stubbingUserServiceFindWithFeedbacksById(loginIdInSession);
		
		Long exerciseId = Long.valueOf(35);
		stubbingExerciseServiceFindWithFeedbacksById(exerciseId);
		
		//When
		feedbackController.addFeedback(exerciseId, saveFeedbackDTO);
		
		//Then
		then(feedbackService).should(times(1)).saveWithUserAndExercise(any(Feedback.class), any(User.class), any(Exercise.class));
	}
	
	@Test
	@DisplayName("Feedback 추가 처리 -> 정상, 리다이렉트 URL 체크")
	void addFeedbackTestCheckRedirectedURL() {
		//Given
		Long loginIdInSession = Long.valueOf(92);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		stubbingUserServiceFindWithFeedbacksById(loginIdInSession);
		
		Long exerciseId = Long.valueOf(35);
		stubbingExerciseServiceFindWithFeedbacksById(exerciseId);
		
		//When
		ModelAndView mav = feedbackController.addFeedback(exerciseId, saveFeedbackDTO);
		
		//Then
		assertEquals("redirect:/exercise/about/"+exerciseId+"/addfeedback", mav.getViewName());
	}
	
	private User stubbingUserServiceFindWithFeedbacksById(Long loginIdInSession) {
		User user = UserTest.getUserInstance();
		given(userService.findWithFeedbacksById(loginIdInSession)).willReturn(user);
		return user;
	}
	
	private Exercise stubbingExerciseServiceFindWithFeedbacksById(Long exerciseId) {
		Exercise exercise = ExerciseTest.getExerciseInstance();
		given(exerciseService.findWithFeedbacksById(exerciseId)).willReturn(exercise);
		return exercise;
	}
}
