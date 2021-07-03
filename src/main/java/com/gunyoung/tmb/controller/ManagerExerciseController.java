package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;


/**
 * 
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerExerciseController {
	
	private final ExerciseService exerciseService;
	
	/**
	 * 
	 * @param page
	 * @param keyword
	 * @param mav
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise",method= RequestMethod.GET)
	public ModelAndView exerciseViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav) {
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
		
		mav.setViewName("exerciseListViewForManage");
		mav.addObject("listObject",resultList);
		mav.addObject("currentPage",page);
		mav.addObject("startIndex",(page/page_size)*page_size+1);
		mav.addObject("lastIndex",(page/page_size)*page_size+page_size-1 > totalPageNum ? totalPageNum : (page/page_size)*page_size+page_size-1);
		
		return mav;
	}
	
	/**
	 * 
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/exercise/add",method = RequestMethod.GET)
	public ModelAndView addExerciseView(ModelAndView mav) {
		List<String> targetTypeKoreanNames = new ArrayList<>();
		
		for(TargetType t: TargetType.values()) {
			targetTypeKoreanNames.add(t.getKoreanName());
		}
		
		mav.setViewName("addExercise");
		mav.addObject("targetTypes", targetTypeKoreanNames);
		
		return mav;
	}
	
	/**
	 * 
	 * @param dto
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/exercise/add" ,method = RequestMethod.POST)
	public ModelAndView addExercise(@ModelAttribute AddExerciseDTO dto,ModelAndView mav) {
		exerciseService.saveWithAddExerciseDTO(dto);
		
		return new ModelAndView("redirect:/manager/exercise");
	}
	
	/**
	 * 
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/exercise/modify", method = RequestMethod.GET) 
	public ModelAndView modifyExerciseView(ModelAndView mav)  {
		return mav;
	}
	
}
