package com.gunyoung.tmb.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

import lombok.RequiredArgsConstructor;

/**
 * Exercise 관련 화면 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@Controller
@RequiredArgsConstructor
public class ExerciseController {
	
	public static final int EXERCISE_LIST_VIEW_PAGE_SIZE = PageSize.EXERCISE_INFO_TABLE_PAGE_SIZE.getSize();
	
	private final ExerciseService exerciseService;
	
	/**
	 * 운동들 리스트 보여주는 화면 반환 <br>
	 * @param keyword 찾고자 하는 운동 이름 키워드
	 * @author kimgun-yeong
	 */
	@GetMapping(value="/exercise")
	public ModelAndView exerciseInfoMainView(@RequestParam(value="page", required=false, defaultValue="1") Integer page,
			@RequestParam(value="keyword", required=false) String keyword, ModelAndPageView mav) {
		Page<Exercise> pageResult = getPageResultForExerciseListView(keyword, page);
		long totalPageNum = getTotalPageNumForExerciseListView(keyword);
		
		List<ExerciseForTableDTO> listObject = ExerciseForTableDTO.of(pageResult);
		
		mav.addObject("listObject",listObject);
		mav.setPageNumbers(page, totalPageNum);
		
		mav.setViewName("exerciseListView");
		
		return mav;
	}

	private Page<Exercise> getPageResultForExerciseListView(String keyword, Integer page) {
		if(keyword == null) {
			return exerciseService.findAllInPage(page, EXERCISE_LIST_VIEW_PAGE_SIZE);
		}
		return exerciseService.findAllWithNameKeywordInPage(keyword, page, EXERCISE_LIST_VIEW_PAGE_SIZE); 
	}
	
	private long getTotalPageNumForExerciseListView(String keyword) {
		if(keyword == null) {
			return exerciseService.countAll()/EXERCISE_LIST_VIEW_PAGE_SIZE +1;
		}
		return exerciseService.countAllWithNameKeyword(keyword)/EXERCISE_LIST_VIEW_PAGE_SIZE +1;
	}
	
	/**
	 * 운동 상세 정보 화면 반환하는 메소드 
	 * @param exerciseId 찾고자하는 Exercise의 ID
	 * @throws ExerciseNotFoundedException 해당 id 의 Exercise 없으면
	 * @author kimgun-yeong
	 */
	@GetMapping(value="/exercise/about/{exercise_id}")
	public ModelAndView exerciseInfoDetailView(@PathVariable("exercise_id") Long exerciseId, ModelAndView mav) {
		ExerciseForInfoViewDTO exerciseforInfoViewDTO = exerciseService.getExerciseForInfoViewDTOByExerciseId(exerciseId);
		if(exerciseforInfoViewDTO == null) {
			throw new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription());
		}
		
		mav.addObject("exerciseInfo", exerciseforInfoViewDTO);
		mav.setViewName("exerciseInfo");
		
		return mav;
	}
}
