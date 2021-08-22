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

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.utils.PageUtil;

import lombok.RequiredArgsConstructor;


/**
 * 매니저의 운동 정보 관리 관련 화면 반환하는 컨트롤러 
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerExerciseController {
	
	private final ExerciseService exerciseService;
	
	/**
	 * Exercise 리스트 화면 반환하는 메소드
	 * @param keyword Exercise Name 검색 키워드
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise",method= RequestMethod.GET)
	public ModelAndView exerciseViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,
			ModelAndPageView mav) {
		int pageSize = PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE;
		
		Page<Exercise> pageResult;
		long totalPageNum;
		
		if(keyword != null) {
			pageResult = exerciseService.findAllWithNameKeywordInPage(keyword, page, pageSize);
			totalPageNum = exerciseService.countAllWithNameKeyword(keyword)/pageSize +1;
		} else {
			pageResult = exerciseService.findAllInPage(page, pageSize);
			totalPageNum = exerciseService.countAll()/pageSize +1;
		}
		
		List<ExerciseForTableDTO> listObject = new ArrayList<>();
		
		for(Exercise e: pageResult) {
			listObject.add(ExerciseForTableDTO.of(e));
		}
		
		mav.addObject("listObject",listObject);
		
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("exerciseListViewForManage");
		
		return mav;
	}
	
	/**
	 * Exercise 추가하는 화면 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/add",method = RequestMethod.GET)
	public ModelAndView addExerciseView(ModelAndView mav) {
		List<String> targetTypeKoreanNames = new ArrayList<>();
		
		for(TargetType t: TargetType.values()) {
			targetTypeKoreanNames.add(t.getKoreanName());
		}
		
		mav.addObject("targetTypes", targetTypeKoreanNames);
		
		mav.setViewName("addExercise");
		
		return mav;
	}
	
	
	/**
	 * Exercise 정보 수정 화면 반환하는 메소드 
	 * @param exerciseId 정보 수정하려는 대상 Exercise
	 * @throws ExerciseNotFoundedException 해당 Id 의 Exercise 없으면
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/modify/{exerciseId}", method = RequestMethod.GET) 
	public ModelAndView modifyExerciseView(@PathVariable("exerciseId") Long exerciseId, ModelAndView mav)  {
		Exercise exercise = exerciseService.findWithExerciseMusclesById(exerciseId);
	
		if(exercise == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		
		SaveExerciseDTO addExerciseDTO = SaveExerciseDTO.of(exercise); 
		
		List<String> targetTypeKoreanNames = new ArrayList<>();
		
		for(TargetType t: TargetType.values()) {
			targetTypeKoreanNames.add(t.getKoreanName());
		}
		
		mav.addObject("exerciseId", exerciseId);
		mav.addObject("exerciseInfo", addExerciseDTO);
		mav.addObject("targetTypes", targetTypeKoreanNames);
		
		mav.setViewName("modifyExercise");
		
		return mav;
	}
	
}
