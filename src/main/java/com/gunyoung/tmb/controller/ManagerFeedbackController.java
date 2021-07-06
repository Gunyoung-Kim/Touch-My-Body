package com.gunyoung.tmb.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.codes.FeedbackErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ManagerFeedbackController {
	
	private final FeedbackService feedbackService;
	
	private final ExerciseService exerciseService;
	
	/**
	 * 
	 * @param exerciseId
	 * @param page
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/exercise/feedback/{exerciseId}" ,method= RequestMethod.GET) 
	public ModelAndView feedbackListView(@PathVariable("exerciseId") Long exerciseId,@RequestParam(value="page", defaultValue="1") int page
			,ModelAndView mav) {
		Exercise exercise = exerciseService.findById(exerciseId);
		
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.ExerciseByIdNotFoundedError.getDescription());
		}
		
		int pageSize = PageUtil.FEEDBACK_FOR_MANAGE_PAGE_SIZE;
		
		Page<FeedbackManageListDTO> pageResult = feedbackService.findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, page, pageSize);
		long totalPageNum = feedbackService.countByExerciseId(exerciseId)/pageSize +1;
		
		mav.addObject("listObject", pageResult);
		mav.addObject("exerciseName", exercise.getName());
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/pageSize)*pageSize+1);
		mav.addObject("lastIndex",(page/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (page/pageSize)*pageSize+pageSize-1);
		
		mav.setViewName("feedbackListViewForManage");
		
		return mav;
	}
	
	/**
	 * 
	 * @param feedbackId
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/exercise/feedback/detail/{feedbackId}" ,method = RequestMethod.GET) 
	public ModelAndView feedbackView(@PathVariable("feedbackId") Long feedbackId, ModelAndView mav) {
		FeedbackViewDTO dto = feedbackService.findForFeedbackViewDTOById(feedbackId);
		
		if(dto == null) {
			throw new FeedbackNotFoundedException(FeedbackErrorCode.FeedbackNotFoundedError.getDescription());
		}
		
		mav.addObject("feedbackInfo", dto);
		mav.setViewName("feedbackView");
		
		return mav;
	}
}
