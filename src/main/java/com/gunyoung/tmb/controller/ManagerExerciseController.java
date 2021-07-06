package com.gunyoung.tmb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.AddExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.duplication.ExerciseNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
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
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/manager/exercise/modify/{exerciseId}", method = RequestMethod.GET) 
	public ModelAndView modifyExerciseView(@PathVariable("exerciseId") Long exerciseId, ModelAndView mav)  {
		Exercise exercise = exerciseService.findWithExerciseMusclesById(exerciseId);
	
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.ExerciseByIdNotFoundedError.getDescription());
		}
		
		AddExerciseDTO dto = AddExerciseDTO.of(exercise); 
		
		List<String> targetTypeKoreanNames = new ArrayList<>();
		
		for(TargetType t: TargetType.values()) {
			targetTypeKoreanNames.add(t.getKoreanName());
		}
		
		mav.addObject("exerciseId", exerciseId);
		mav.addObject("exerciseInfo", dto);
		mav.addObject("targetTypes", targetTypeKoreanNames);
		
		mav.setViewName("modifyExercise");
		return mav;
	}
	
	/**
	 * 
	 * @param exerciseId
	 * @param dto
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/modify/{exerciseId}",method=RequestMethod.POST) 
	public ModelAndView modifyExercise(@PathVariable("exerciseId") Long exerciseId, @ModelAttribute AddExerciseDTO dto) {
		Exercise exercise = exerciseService.findById(exerciseId);
		
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.ExerciseByIdNotFoundedError.getDescription());
		}
		
		if(!exercise.getName().equals(dto.getName()) && exerciseService.existsByName(dto.getName())) {
			throw new ExerciseNameDuplicationFoundedException(ExerciseErrorCode.ExerciseNameDuplicatedError.getDescription());
		}
		
		exerciseService.saveWithAddExerciseDTO(exercise, dto);
		
		return new ModelAndView("redirect:/manager/exercise"); 
	}
	
}
