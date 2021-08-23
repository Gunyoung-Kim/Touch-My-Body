package com.gunyoung.tmb.controller.rest.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.servlet.ModelAndView;

import com.gunyoung.tmb.controller.ManagerExerciseController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;

/**
 * {@link ManagerExerciseController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerExerciseController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ManagerExerciseControllerUnitTest {
	
	@Mock
	ExerciseService exerciseService;
	
	@InjectMocks
	ManagerExerciseController managerExerciseController;
	
	private Integer defaultPageNum = 1;
	
	private ModelAndPageView mapv;
	private ModelAndView mav;
	
	@BeforeEach
	void setup() {
		mapv = mock(ModelAndPageView.class);
		mav = mock(ModelAndView.class);
	}
	
	/*
	 * public ModelAndView exerciseViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,
	 *		@RequestParam(value="keyword",required=false) String keyword, ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("Exercise 리스트 화면 반환 -> 정상, 키워드 없음, exerciseService check")
	public void exerciseViewForManagerTestNoKeywordCheckExerciseService() {
		//Given
		Page<Exercise> pageResult = new PageImpl<>(new ArrayList<>());
		given(exerciseService.findAllInPage(defaultPageNum, ManagerExerciseController.EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE)).willReturn(pageResult);
		
		//When
		managerExerciseController.exerciseViewForManager(defaultPageNum, null, mapv);
		
		//Then
		then(exerciseService).should(times(1)).findAllInPage(defaultPageNum, ManagerExerciseController.EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
		then(exerciseService).should(times(1)).countAll();
	}
	
	@Test
	@DisplayName("Exercise 리스트 화면 반환 -> 정상, 키워드 있음, exerciseService check")
	public void exerciseViewForManagerTestWithKeywordCheckExerciseService() {
		//Given
		String keyword = "keyword";
		Page<Exercise> pageResult = new PageImpl<>(new ArrayList<>());
		given(exerciseService.findAllWithNameKeywordInPage(keyword, defaultPageNum, ManagerExerciseController.EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE)).willReturn(pageResult);
		
		//When
		managerExerciseController.exerciseViewForManager(defaultPageNum, keyword, mapv);
		
		//Then
		then(exerciseService).should(times(1)).findAllWithNameKeywordInPage(keyword, defaultPageNum, ManagerExerciseController.EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
		then(exerciseService).should(times(1)).countAllWithNameKeyword(keyword);
	}
	
	@Test
	@DisplayName("Exercise 리스트 화면 반환 -> 정상, 키워드 없음, ModelAndPageView check")
	public void exerciseViewForManagerTestNoKeywordCheckMapv() {
		//Given
		Page<Exercise> pageResult = new PageImpl<>(new ArrayList<>());
		given(exerciseService.findAllInPage(defaultPageNum, ManagerExerciseController.EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE)).willReturn(pageResult);
		
		Long givenExerciseNum = Long.valueOf(25);
		given(exerciseService.countAll()).willReturn(givenExerciseNum);
		
		//When
		managerExerciseController.exerciseViewForManager(defaultPageNum, null, mapv);
		
		//Then
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenExerciseNum / ManagerExerciseController.EXERCISE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("exerciseListViewForManage");
	}
	
	/*
	 * public ModelAndView addExerciseView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("Exercise 추가하는 화면 반환 -> 정상, ModelAndView check")
	public void addExerciseViewTestCheckMav() {
		//Given
		
		//When
		managerExerciseController.addExerciseView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("addExercise");
	}
	
}
