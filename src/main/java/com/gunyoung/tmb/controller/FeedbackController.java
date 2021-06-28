package com.gunyoung.tmb.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.AddFeedbackDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.UserErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.UserNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.services.domain.user.UserService;
import com.gunyoung.tmb.utils.SessionUtil;

@Controller
public class FeedbackController {
	
	@Autowired
	FeedbackService feedbackService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ExerciseService exerciseService;
	
	@Autowired
	HttpSession session;
	
	/**
	 * 
	 * @param exerciseId
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/exercise/about/{exercise_id}/addfeedback",method=RequestMethod.GET)
	public ModelAndView addFeedbackView(@PathVariable("exercise_id") Long exerciseId,ModelAndView mav) {
		Exercise exercise = exerciseService.findById(exerciseId);
		
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.ExerciseByIdNotFoundedError.getDescription());
		}
		
		mav.setViewName("addFeedback");
		mav.addObject("exerciseName", exercise.getName());
		return mav;
	}
	
	@RequestMapping(value="/exercise/about/{exercise_id}/addfeedback",method=RequestMethod.POST)
	public ModelAndView addFeedback(@PathVariable("exercise_id") Long exerciseId,@ModelAttribute AddFeedbackDTO dto) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		User user = userService.findById(userId);
		
		if(userId == null) {
			throw new UserNotFoundedException(UserErrorCode.UserNotFoundedError.getDescription());
		}
		
		Exercise exercise = exerciseService.findById(exerciseId);
		
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.ExerciseByIdNotFoundedError.getDescription());
		}
		
		Feedback feedback = Feedback.builder()
				.title(dto.getTitle())
				.content(dto.getContent())
				.build();
		
		feedbackService.saveWithUserAndExercise(feedback, user, exercise);
		
		return new ModelAndView("redirect:/exercise/about/"+exerciseId+"/addfeedback");
	}
}
