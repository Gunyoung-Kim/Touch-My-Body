package com.gunyoung.tmb.controller;

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
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

import lombok.RequiredArgsConstructor;


/**
 * 매니저의 운동 정보 관리 관련 화면 반환하는 컨트롤러 
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ManagerExerciseController {
	
	public static final int EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE = PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize();
	
	private final ExerciseService exerciseService;
	
	/**
	 * Exercise 리스트 화면 반환하는 메소드
	 * @param keyword Exercise Name 검색 키워드
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise",method= RequestMethod.GET)
	public ModelAndView exerciseViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,
			@RequestParam(value="keyword",required=false) String keyword, ModelAndPageView mav) {
		Page<Exercise> pageResult = getPageResultForExerciseListViewForManager(keyword, page);
		long totalPageNum = getTotalPageNumForExerciseListViewForManager(keyword);
		
		List<ExerciseForTableDTO> listObject = ExerciseForTableDTO.of(pageResult);
		
		mav.addObject("listObject",listObject);
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("exerciseListViewForManage");
		
		return mav;
	}
	
	private Page<Exercise> getPageResultForExerciseListViewForManager(String keyword, Integer page) {
		if(keyword == null) {
			return exerciseService.findAllInPage(page, EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
		}
		return exerciseService.findAllWithNameKeywordInPage(keyword, page, EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
	}
	
	private long getTotalPageNumForExerciseListViewForManager(String keyword) {
		if(keyword == null) {
			return exerciseService.countAll()/EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE +1;
		}
		return exerciseService.countAllWithNameKeyword(keyword)/EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE +1;
	}
	
	/**
	 * Exercise 추가하는 화면 반환하는 메소드
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/manager/exercise/add",method = RequestMethod.GET)
	public ModelAndView addExerciseView(ModelAndView mav) {
		List<String> koreanNamesForAllTargetType = TargetType.getKoreanNamesForAllTargetType();
		
		mav.addObject("targetTypes", koreanNamesForAllTargetType);
		
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

		List<String> koreanNamesForAllTargetType = TargetType.getKoreanNamesForAllTargetType();
		
		mav.addObject("exerciseId", exerciseId);
		mav.addObject("exerciseInfo", addExerciseDTO);
		mav.addObject("targetTypes", koreanNamesForAllTargetType);
		
		mav.setViewName("modifyExercise");
		
		return mav;
	}
}
