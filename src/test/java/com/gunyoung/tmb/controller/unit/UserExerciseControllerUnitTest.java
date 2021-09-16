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

import com.gunyoung.tmb.controller.UserExerciseController;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.SaveUserExerciseDTO;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.UserExerciseTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserExerciseController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserExerciseController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserExerciseControllerUnitTest {
	
	@Mock
	HttpSession session;
	
	@Mock
	UserService userService;
	
	@Mock
	ExerciseService exerciseService;
	
	@InjectMocks
	UserExerciseController userExerciseController;
	
	private ModelAndView mav;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
	}
	
	/*
	 * public ModelAndView calendarView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("User의 그간의 운동 기록을 보여주는 캘린더 화면 반환 -> 정상, View name check")
	public void calendarViewTestCheckViewName() {
		//Given
		
		//When
		userExerciseController.calendarView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("exerciseCalendar");
	}
	
	/*
	 * public ModelAndView addUserExerciseView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("User의 오늘의 운동 기록을 추가하는 화면 반환 -> 정상, View Name check")
	public void addUserExerciseViewTestCheckViewName() {
		//Given
		
		//When
		userExerciseController.addUserExerciseView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("addRecord");
	}
	
	
	/*
	 * public ModelAndView addUserExercise(@ModelAttribute("formModel") SaveUserExerciseDTO formModel)
	 */
	
	@Test
	@DisplayName("User의 오늘의 운동 기록 추가 처리 -> 접속된 세션의 id의 user 가 없을 때 ")
	public void addUserExerciseUserNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(1);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(nonExistUserId);
		given(userService.findWithUserExerciseById(nonExistUserId)).willReturn(null);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		SaveUserExerciseDTO saveUserExerciseDTO = UserExerciseTest.getSaveUserExerciseDTOInstance(exercise.getName()); 
		
		//When, Then
		assertThrows(UserNotFoundedException.class, () -> {
			userExerciseController.addUserExercise(saveUserExerciseDTO);
		});
	}
	
	@Test
	@DisplayName("User의 오늘의 운동 기록 추가 처리 -> 해당 이름을 만족하는 Exercise 가 없을 때")
	public void addUserExerciseExerciseNonExist() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionAndUserServiceFindWithUserExerciseById(loginIdInSession);
		
		String nonExistExerciseName = "nonExist";
		SaveUserExerciseDTO saveUserExerciseDTO = UserExerciseTest.getSaveUserExerciseDTOInstance(nonExistExerciseName);
		
		given(exerciseService.findByName(nonExistExerciseName)).willReturn(null);
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			userExerciseController.addUserExercise(saveUserExerciseDTO);
		});
	}
	
	@Test
	@DisplayName("User의 오늘의 운동 기록 추가 처리 -> Check UserService")
	public void addUserExerciseTestCheckUserService() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionAndUserServiceFindWithUserExerciseById(loginIdInSession);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		SaveUserExerciseDTO saveUserExerciseDTO = UserExerciseTest.getSaveUserExerciseDTOInstance(exercise.getName());
		
		given(exerciseService.findByName(exercise.getName())).willReturn(exercise);
		
		//When
		userExerciseController.addUserExercise(saveUserExerciseDTO);
		
		//Then
		then(userService).should(times(1)).addUserExercise(any(User.class), any(UserExercise.class));
	}
	
	@Test
	@DisplayName("User의 오늘의 운동 기록 추가 처리 -> Check ModelAndView")
	public void addUserExerciseTestCheckModelAndView() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		stubbingSessionAndUserServiceFindWithUserExerciseById(loginIdInSession);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		SaveUserExerciseDTO saveUserExerciseDTO = UserExerciseTest.getSaveUserExerciseDTOInstance(exercise.getName());
		
		given(exerciseService.findByName(exercise.getName())).willReturn(exercise);
		
		//When
		ModelAndView result = userExerciseController.addUserExercise(saveUserExerciseDTO);
		
		//Then
		assertEquals("redirect:/user/exercise/calendar/addrecord", result.getViewName());
	}
	
	private User stubbingSessionAndUserServiceFindWithUserExerciseById(Long loginIdInSession) {
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		User user = UserTest.getUserInstance();
		given(userService.findWithUserExerciseById(loginIdInSession)).willReturn(user);
		return user;
	}
	
}
