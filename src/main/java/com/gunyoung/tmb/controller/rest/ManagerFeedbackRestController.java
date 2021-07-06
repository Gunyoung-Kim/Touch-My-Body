package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.error.codes.FeedbackErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerFeedbackRestController {
	
	private final FeedbackService feedbackService;
	
	/**
	 * 
	 * @param feedbackId
	 */
	@RequestMapping(value="/manager/exercise/feedback/reflect/{feedbackId}", method = RequestMethod.PATCH)
	public void reflectFeedback(@PathVariable("feedbackId") Long feedbackId) {
		Feedback feedback = feedbackService.findById(feedbackId);
		
		if(feedback == null) {
			throw new FeedbackNotFoundedException(FeedbackErrorCode.FeedbackNotFoundedError.getDescription());
		}
		
		feedback.setReflected(true);
		
		feedbackService.save(feedback);
	}
}
