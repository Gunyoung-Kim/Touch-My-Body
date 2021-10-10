package com.gunyoung.tmb.controller.unit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.ManagerExercisePostController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostService;

/**
 * {@link ManagerExercisePostController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerExercisePostController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ManagerExercisePostControllerUnitTest {
	
	@Mock
	ExercisePostService exercisePostService;
	
	@InjectMocks 
	ManagerExercisePostController managerExercisePostController;
	
	private ModelAndPageView mapv;
	
	@BeforeEach
	void setup() {
		mapv = mock(ModelAndPageView.class);
	}
	
	/*
	 * ModelAndView manageCommunityView(@RequestParam(value="page", defaultValue="1") int page,
     *			@RequestParam(value ="keyword", required=false) String keyword,ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("ExercisePost 리스트 화면 반환 -> 정상, 키워드 포함")
	void manageCommunityViewTestWithKeyword() {
		//Given
		int pageNum = 1;
		String keyword = "keyword";
		
		//When
		managerExercisePostController.manageCommunityView(pageNum, keyword, mapv);
		
		//Then
		then(exercisePostService).should(times(1)).findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, pageNum, PageSize.POST_FOR_MANAGE_PAGE_SIZE.getSize());
		then(exercisePostService).should(times(1)).countWithTitleAndContentsKeyword(keyword);
	}
	
	@Test
	@DisplayName("ExercisePost 리스트 화면 반환 -> 정상, 키워드 없을 때")
	void manageCommunityViewTestNoKeyword() {
		//Given
		int pageNum = 1;
		
		//When
		managerExercisePostController.manageCommunityView(pageNum, null, mapv);
		
		//Then
		verifyManageCommunityViewTestNoKeyword(pageNum);
	}
	
	private void verifyManageCommunityViewTestNoKeyword(int pageNum) {
		then(exercisePostService).should(never()).findAllForPostForCommunityViewDTOWithKeywordByPage(anyString(), anyInt(), anyInt());
		then(exercisePostService).should(never()).countWithTitleAndContentsKeyword(anyString());
		then(mapv).should(times(1)).setPageNumbers(pageNum, Long.valueOf(1));
	}
	
	@Test
	@DisplayName("ExercisePost 리스트 화면 반환 -> 정상, View name 확인")
	void manageCommunityViewTestCheckViewName() {
		//Given
		int pageNum = 1;
		String keyword = "keyword";
		
		//When
		managerExercisePostController.manageCommunityView(pageNum, keyword, mapv);
		
		//Then
		then(mapv).should(times(1)).setViewName("communityForManage");
	}
}
