package com.gunyoung.tmb.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.FeedbackErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;

import lombok.RequiredArgsConstructor;

/**
 * 매니저의 피드백 관리 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerFeedbackController {
	
	public static final int FEEDBACK_FOR_MANAGE_PAGE_SIZE = PageSize.FEEDBACK_FOR_MANAGE_PAGE_SIZE.getSize();
	
	private final FeedbackService feedbackService;
	
	private final ExerciseService exerciseService;
	
	/**
	 * 특정 Exercise 정보에 대해 작성된 Feedback 리스트 화면 반환하는 메소드
	 * @param exerciseId Feedback들의 대상 Exercise의 ID
	 * @throws ExerciseNotFoundedException 해당 Id의 Exercise 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/feedback/{exerciseId}" ,method= RequestMethod.GET) 
	public ModelAndView feedbackListView(@PathVariable("exerciseId") Long exerciseId,@RequestParam(value="page", defaultValue="1") int page
			,ModelAndPageView mav) {
		Exercise exercise = exerciseService.findById(exerciseId);
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		
		int pageSize = FEEDBACK_FOR_MANAGE_PAGE_SIZE;
		
		Page<FeedbackManageListDTO> pageResult = feedbackService.findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, page, pageSize);
		long totalPageNum = feedbackService.countByExerciseId(exerciseId)/pageSize +1;
		
		mav.addObject("listObject", pageResult);
		mav.addObject("exerciseName", exercise.getName());
		
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("feedbackListViewForManage");
		
		return mav;
	}
	
	/**
	 * 특정 Feedback의 상세정보 화면 반환하는 메소드
	 * @param feedbackId 상세정보 열람하고자 하는 대상 Feedback의 ID
	 * @throws FeedbackNotFoundedException 해당 Id의 Feedback 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/feedback/detail/{feedbackId}" ,method = RequestMethod.GET) 
	public ModelAndView feedbackView(@PathVariable("feedbackId") Long feedbackId, ModelAndView mav) {
		FeedbackViewDTO feedbackViewDTO = feedbackService.findForFeedbackViewDTOById(feedbackId);
		if(feedbackViewDTO == null) {
			throw new FeedbackNotFoundedException(FeedbackErrorCode.FEEDBACK_NOT_FOUNDED_ERROR.getDescription());
		}
		
		mav.addObject("feedbackInfo", feedbackViewDTO);
		
		mav.setViewName("feedbackView");
		
		return mav;
	}
}
