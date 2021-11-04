package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.testutil.ExerciseTest;

/**
 * {@link ManagerExerciseController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerExerciseController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ManagerExerciseControllerUnitTest {
	
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
	 * ModelAndView exerciseViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,
	 *		@RequestParam(value="keyword",required=false) String keyword, ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("Exercise 리스트 화면 반환 -> 정상, 키워드 없음, exerciseService check")
	void exerciseViewForManagerTestNoKeywordCheckExerciseService() {
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
	void exerciseViewForManagerTestWithKeywordCheckExerciseService() {
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
	void exerciseViewForManagerTestNoKeywordCheckMapv() {
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
	 * ModelAndView addExerciseView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("Exercise 추가하는 화면 반환 -> 정상, ModelAndView check")
	void addExerciseViewTestCheckMav() {
		//Given
		
		//When
		managerExerciseController.addExerciseView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("addExercise");
	}
	
	/*
	 * ModelAndView modifyExerciseView(@PathVariable("exerciseId") Long exerciseId, ModelAndView mav) 
	 */
	
	@Test
	@DisplayName("Exercise 정보 수정 화면 반환 -> 해당 Id 의 Exercise 없으면")
	void modifyExerciseViewExerciseNonExist() {
		//Given
		Long nonExistExerciseId = Long.valueOf(24);
		given(exerciseService.findWithExerciseMusclesById(nonExistExerciseId)).willThrow(new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class , () -> {
			managerExerciseController.modifyExerciseView(nonExistExerciseId, mav);
		});
	}
	
	@Test
	@DisplayName("Exercise 정보 수정 화면 반환 -> 정상, ModelAndView check")
	void modifyExerciseViewTestCheckMav() {
		//Given
		Long exerciseId = Long.valueOf(36);
		Exercise exercise = ExerciseTest.getExerciseInstance();
		given(exerciseService.findWithExerciseMusclesById(exerciseId)).willReturn(exercise);
		
		//When
		managerExerciseController.modifyExerciseView(exerciseId, mav);
		
		//Then
		then(mav).should(times(1)).addObject("exerciseId", exerciseId);
		then(mav).should(times(1)).setViewName("modifyExercise");
	}
	
}
