package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExerciseController {
	
	private final ExerciseService exerciseService;
	
	/**
	 * 운동들 리스트 보여주는 화면 반환 
	 * @param page
	 * @param keyword
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/exercise",method = RequestMethod.GET)
	public ModelAndView exerciseInfoMainView(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav) {
		int page_size = PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE;
		
		Page<Exercise> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = exerciseService.findAllWithNameKeywordInPage(keyword, page, page_size);
			totalPageNum = exerciseService.countAllWithNameKeyword(keyword)/page_size +1;
		} else {
			pageResult = exerciseService.findAllInPage(page, page_size);
			totalPageNum = exerciseService.countAll()/page_size +1;
		}
		
		List<ExerciseForTableDTO> resultList = new ArrayList<>();
		
		for(Exercise e: pageResult) {
			resultList.add(ExerciseForTableDTO.of(e));
		}
		
		mav.setViewName("exerciseListView");
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;
	}
	
	@RequestMapping(value="/exercise/about/{exercise_id}", method = RequestMethod.GET)
	public ModelAndView exerciseInfoMainView(@PathVariable("exercise_id") Long exerciseId,ModelAndView mav) {
		
		ExerciseForInfoViewDTO dto = exerciseService.getExerciseForInfoViewDTOByExerciseId(exerciseId);
		
		if(dto == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		
		mav.setViewName("exerciseInfo");
		mav.addObject("exerciseInfo", dto);
		
		return mav;
	}
}
