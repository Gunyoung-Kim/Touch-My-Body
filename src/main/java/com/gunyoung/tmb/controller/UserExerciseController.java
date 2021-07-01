package com.gunyoung.tmb.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.AddUserExerciseDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserExerciseController {
	
	private final HttpSession session;
	
	private final UserService userService;
	
	private final ExerciseService exerciseService;
	
	/**
	 * User의 그간의 운동 기록을 보여주는 캘린더 
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar",method = RequestMethod.GET)
	public ModelAndView calendarView(ModelAndView mav) {
		mav.setViewName("exerciseCalendar");
		
		return mav;
	}
	
	/**
	 * User의 오늘의 운동 기록을 추가하는 뷰 반환하는 메소드
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/addrecord",method = RequestMethod.GET)
	public ModelAndView addUserExerciseView(ModelAndView mav) {
		mav.setViewName("addRecord");
		
		return mav;
	}
	
	/**
	 * User의 오늘의 운동 기록 추가 처리하는 메소드
	 * @param mav
	 * @return
	 * @throws UserNotFoundedException - 접속된 세션의 id의 user 가 없을 때
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/addrecord",method = RequestMethod.POST)
	public ModelAndView addUserExercise(@ModelAttribute("formModel") AddUserExerciseDTO formModel) {
		Long userId = SessionUtil.getLoginUserId(session);
		//유저와 유저 운동 기록 페치 조인으로 가져옴
		User user = userService.findWithUserExerciseById(userId);
		
		if(user == null) 
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		
		UserExercise userExercise = AddUserExerciseDTO.toUserExercise(formModel);
		
		Exercise exercise = exerciseService.findByName(formModel.getExerciseName());
		
		if(exercise == null)
			throw new ExerciseNotFoundedException(ExerciseErrorCode.ExerciseByNameNotFoundedError.getDescription());
		
		userExercise.setExercise(exercise);
		
		userService.addUserExercise(user, userExercise);
		
		return new ModelAndView("redirect:/user/exercise/calendar/addrecord");
	}
	
}
