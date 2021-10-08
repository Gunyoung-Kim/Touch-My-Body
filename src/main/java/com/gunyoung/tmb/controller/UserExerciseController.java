package com.gunyoung.tmb.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.SaveUserExerciseDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * UserExercise 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class UserExerciseController {
	
	private final HttpSession session;
	
	private final UserService userService;
	
	private final ExerciseService exerciseService;
	
	/**
	 * User의 그간의 운동 기록을 보여주는 캘린더 화면 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/user/exercise/calendar")
	public ModelAndView calendarView(ModelAndView mav) {
		mav.setViewName("exerciseCalendar");
		
		return mav;
	}
	
	/**
	 * User의 오늘의 운동 기록을 추가하는 화면 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@GetMapping(value = "/user/exercise/calendar/addrecord")
	public ModelAndView addUserExerciseView(ModelAndView mav) {
		mav.setViewName("addRecord");
		
		return mav;
	}
	
	/**
	 * User의 오늘의 운동 기록 추가 처리하는 메소드
	 * @throws UserNotFoundedException 접속된 세션의 id의 user 가 없을 때
	 * @throws ExerciseNotFoundedException 해당 이름을 만족하는 Exercise 가 없을 때
	 * @author kimgun-yeong
	 */
	@PostMapping(value = "/user/exercise/calendar/addrecord")
	@LoginIdSessionNotNull
	public ModelAndView addUserExercise(@ModelAttribute("formModel") SaveUserExerciseDTO formModel) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findWithUserExerciseById(loginUserId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		UserExercise userExercise = SaveUserExerciseDTO.toUserExercise(formModel);
		
		Exercise exercise = exerciseService.findByName(formModel.getExerciseName());
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_NAME_NOT_FOUNDED_ERROR.getDescription());
		}
		userExercise.setExercise(exercise);
		
		userService.addUserExercise(user, userExercise);
		
		return new ModelAndView("redirect:/user/exercise/calendar/addrecord");
	}
	
}
