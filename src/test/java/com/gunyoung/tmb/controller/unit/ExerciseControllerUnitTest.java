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

import com.gunyoung.tmb.controller.ExerciseController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.error.codes.ExerciseErrorCode;
import com.gunyoung.tmb.error.exceptions.nonexist.ExerciseNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.testutil.ExerciseTest;

/**
 * {@link ExerciseController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExerciseController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ExerciseControllerUnitTest {
	
	@Mock
	ExerciseService exerciseService;
	
	@InjectMocks
	ExerciseController exerciseController;
	
	private Integer defaultPageNum = 1;
	
	private ModelAndPageView mapv;
	private ModelAndView mav;
	
	@BeforeEach 
	void setup() {
		mapv = mock(ModelAndPageView.class);
		mav = mock(ModelAndView.class);
	}
	
	/*
	 * ModelAndView exerciseInfoMainView(@RequestParam(value="page", required=false, defaultValue="1") Integer page, @RequestParam(value="keyword", required=false) String keyword
	 *		, ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("운동들 리스트 보여주는 화면 반환 -> 정상, 키워드 없음")
	void exerciseInfoMainViewTestNoKeyword() {
		//Given
		stubbingExerciseServiceFindAllInPageWithEmptyPage();
		given(exerciseService.countAll()).willReturn(Long.valueOf(0));
		
		//When
		exerciseController.exerciseInfoMainView(defaultPageNum, null, mapv);
		
		//Then
		then(exerciseService).should(times(1)).findAllInPage(defaultPageNum, ExerciseController.EXERCISE_LIST_VIEW_PAGE_SIZE);
		then(exerciseService).should(times(1)).countAll();
	}
	
	@Test
	@DisplayName("운동들 리스트 보여주는 화면 반환 -> 정상, 키워드 있음")
	void exerciseInfoMainViewWithkeyword() {
		//Given
		String keyword = "keyword";
		stubbingExerciseServiceFindAllWithNameKeywordInPageWithEmptyPage(keyword);
		long givenExerciseNumWithKeyword = 10;
		given(exerciseService.countAllWithNameKeyword(keyword)).willReturn(givenExerciseNumWithKeyword);
		
		//When
		exerciseController.exerciseInfoMainView(defaultPageNum, keyword, mapv);
		
		//Then
		then(exerciseService).should(times(1)).findAllWithNameKeywordInPage(keyword, defaultPageNum, ExerciseController.EXERCISE_LIST_VIEW_PAGE_SIZE);
	}
	
	@Test
	@DisplayName("운동들 리스트 보여주는 화면 반환 -> 정상, ModelAndPageView check")
	void exerciseInfoMainViewTestCheckMapv() {
		//Given
		String keyword = "keyword";
		stubbingExerciseServiceFindAllWithNameKeywordInPageWithEmptyPage(keyword);
		long givenExerciseNumWithKeyword = 10;
		given(exerciseService.countAllWithNameKeyword(keyword)).willReturn(givenExerciseNumWithKeyword);
		
		//When
		exerciseController.exerciseInfoMainView(defaultPageNum, keyword, mapv);
		
		//Then
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenExerciseNumWithKeyword/ ExerciseController.EXERCISE_LIST_VIEW_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("exerciseListView");
	}
	
	private void stubbingExerciseServiceFindAllWithNameKeywordInPageWithEmptyPage(String keyword) {
		Page<Exercise> pageResult = new PageImpl<>(new ArrayList<>());
		given(exerciseService.findAllWithNameKeywordInPage(keyword, defaultPageNum, ExerciseController.EXERCISE_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
	}
	
	private void stubbingExerciseServiceFindAllInPageWithEmptyPage() {
		Page<Exercise> pageResult = new PageImpl<>(new ArrayList<>());
		given(exerciseService.findAllInPage(defaultPageNum, ExerciseController.EXERCISE_LIST_VIEW_PAGE_SIZE)).willReturn(pageResult);
	}
	
	
	/*
	 * ModelAndView exerciseInfoDetailView(@PathVariable("exercise_id") Long exerciseId, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("운동 상세 정보 화면 반환 -> 해당 id 의 Exercise 없으면")
	void exerciseInfoDetailViewExerciseNonExist() {
		//Given
		Long nonExistExerciseId = Long.valueOf(1);
		given(exerciseService.getExerciseForInfoViewDTOByExerciseId(nonExistExerciseId)).willThrow(new ExerciseNotFoundedException(ExerciseErrorCode.EXERCISE_BY_ID_NOT_FOUNDED_ERROR.getDescription()));
		
		//When, Then
		assertThrows(ExerciseNotFoundedException.class, () -> {
			exerciseController.exerciseInfoDetailView(nonExistExerciseId, mav);
		});
	}
	
	@Test
	@DisplayName("운동 상세 정보 화면 반환 -> 정상, Check ModelAndView")
	void exerciseInfoDetailViewTestCheckModelAndView() {
		//Given
		Long exerciseId = Long.valueOf(1);
		ExerciseForInfoViewDTO exerciseForInfoViewDTO = ExerciseTest.getExerciseForInfoViewDTOInstance();
		given(exerciseService.getExerciseForInfoViewDTOByExerciseId(exerciseId)).willReturn(exerciseForInfoViewDTO);
		
		//When
		exerciseController.exerciseInfoDetailView(exerciseId, mav);
		
		//Then
		then(mav).should(times(1)).addObject("exerciseInfo", exerciseForInfoViewDTO);
		then(mav).should(times(1)).setViewName("exerciseInfo");
	}
}
