package com.gunyoung.tmb.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
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
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * Feedback 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class FeedbackController {
	
	private final FeedbackService feedbackService;
	
	private final UserService userService;
	
	private final ExerciseService exerciseService;
	
	private final HttpSession session;
	
	/**
	 * Feedback 추가 화면 반환하는 메소드
	 * @param exerciseId Feedback 추가하려는 Exercise의 ID
	 * @throws ExerciseNotFoundedException 해당 ID의 Exercise 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/exercise/about/{exercise_id}/addfeedback",method=RequestMethod.GET)
	public ModelAndView addFeedbackView(@PathVariable("exercise_id") Long exerciseId, ModelAndView mav) {
		Exercise exercise = exerciseService.findById(exerciseId);
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		
		mav.addObject("exerciseName", exercise.getName());
		
		mav.setViewName("addFeedback");
		
		return mav;
	}
	
	/**
	 * Feedback 추가 처리하는 메소드 <br>
	 * 추가 성공 시 Feedback 추가 화면으로 리다이렉트
	 * @param exerciseId Feedback 추가하려는 Exercise의 ID
	 * @throws UserNotFoundedException 세션에 저장된 ID에 해당하는 User 없으면
	 * @throws ExerciseNotFoundedException 해당 ID에 해당하는 Exercise 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/exercise/about/{exercise_id}/addfeedback",method=RequestMethod.POST)
	@LoginIdSessionNotNull
	public ModelAndView addFeedback(@PathVariable("exercise_id") Long exerciseId, @ModelAttribute SaveFeedbackDTO dto) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		User user = userService.findWithFeedbacksById(loginUserId);
		if(user == null) {
			throw new UserNotFoundedException(UserErrorCode.USER_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Exercise exercise = exerciseService.findWithFeedbacksById(exerciseId);
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Feedback feedback = dto.createFeedback();
		
		feedbackService.saveWithUserAndExercise(feedback, user, exercise);
		
		return new ModelAndView("redirect:/exercise/about/"+exerciseId+"/addfeedback");
	}
}
