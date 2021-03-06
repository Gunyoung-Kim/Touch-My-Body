package com.gunyoung.tmb.controller.rest;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 Feedbacka 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class ManagerFeedbackRestController {
	
	private final FeedbackService feedbackService;
	
	/**
	 * 매니저의 특정 Feedback 반영 요청 처리하는 메소드
	 * @param feedbackId 반영 처리하려는 대상 Feedback의 Id
	 * @throws FeedbackNotFoundedException 해당 Id의 Feedback 없으면
	 * @author kimgun-yeong
	 */
	@PatchMapping(value = "/manager/exercise/feedback/reflect/{feedbackId}")
	public void reflectFeedback(@PathVariable("feedbackId") Long feedbackId) {
		Feedback feedback = feedbackService.findById(feedbackId);
		feedback.setReflected(true);
		feedbackService.save(feedback);
	}
}
