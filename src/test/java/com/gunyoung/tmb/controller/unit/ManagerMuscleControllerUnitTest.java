package com.gunyoung.tmb.controller.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.gunyoung.tmb.controller.ManagerMuscleController;
import com.gunyoung.tmb.controller.util.ModelAndPageView;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.duplication.MuscleNameDuplicationFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.MuscleNotFoundedException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;
import com.gunyoung.tmb.services.domain.exercise.MuscleService;
import com.gunyoung.tmb.testutil.MuscleTest;

/**
 * {@link ManagerMuscleController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ManagerMuscleController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ManagerMuscleControllerUnitTest {
	
	@Mock
	MuscleService muscleService;
	
	@InjectMocks
	ManagerMuscleController managerMuscleController;
	
	private Integer defaultPageNum = 1;
	
	private ModelAndView mav;
	private ModelAndPageView mapv;
	
	@BeforeEach
	void setup() {
		mav = mock(ModelAndView.class);
		mapv = mock(ModelAndPageView.class);
	}
	
	/*
	 * public ModelAndView muscleViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,
	 *		ModelAndPageView mav)
	 */
	
	@Test
	@DisplayName("Muscle의 리스트 화면 반환 -> 정상, 키워드 없음, muscleService check")
	public void muscleViewForManagerTestNoKeywordCheckMuscleService() {
		//Given
		stubbingMuscleServiceFindAllInPageWithEmptyPage();
		
		//When
		managerMuscleController.muscleViewForManager(defaultPageNum, null, mapv);
		
		//Then
		then(muscleService).should(times(1)).findAllInPage(defaultPageNum, ManagerMuscleController.MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
		then(muscleService).should(times(1)).countAll();
	}
	
	private void stubbingMuscleServiceFindAllInPageWithEmptyPage() {
		Page<Muscle> pageResult = new PageImpl<>(new ArrayList<>());
		given(muscleService.findAllInPage(defaultPageNum, ManagerMuscleController.MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE)).willReturn(pageResult);
	}
	
	@Test
	@DisplayName("Muscle의 리스트 화면 반환 -> 정상, 키워드 있음, muscleService check")
	public void muscleViewForManagerTestYesKeywordCheckMuscleService() {
		//Given
		String keyword = "keyword";
		stubbingMuscleServiceFindAllWithNameKeywordInPage(keyword);
		
		//When
		managerMuscleController.muscleViewForManager(defaultPageNum, keyword, mapv);
		
		//Then
		then(muscleService).should(times(1)).findAllWithNameKeywordInPage(keyword, defaultPageNum, ManagerMuscleController.MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE);
		then(muscleService).should(times(1)).countAllWithNameKeyword(keyword);
	}
	
	@Test
	@DisplayName("Muscle의 리스트 화면 반환 -> 정상, ModelAndPageView Check")
	public void muscleViewForManagerTestCheckMapv() {
		//Given
		String keyword = "keyword";
		stubbingMuscleServiceFindAllWithNameKeywordInPage(keyword);
		
		Long givenMuscleNumWithKeyword = Long.valueOf(16);
		given(muscleService.countAllWithNameKeyword(keyword)).willReturn(givenMuscleNumWithKeyword);
		
		//When
		managerMuscleController.muscleViewForManager(defaultPageNum, keyword, mapv);
		
		//Then
		then(mapv).should(times(1)).setPageNumbers(defaultPageNum, givenMuscleNumWithKeyword / ManagerMuscleController.MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE + 1);
		then(mapv).should(times(1)).setViewName("muscleListViewForManage");
	}
	
	private void stubbingMuscleServiceFindAllWithNameKeywordInPage(String keyword) {
		Page<Muscle> pageResult = new PageImpl<>(new ArrayList<>());
		given(muscleService.findAllWithNameKeywordInPage(keyword, defaultPageNum, ManagerMuscleController.MUSCLE_LIST_VIEW_FOR_MANAGE_PAGE_SIZE)).willReturn(pageResult);
	}
	
	/*
	 * public ModelAndView addMuscleView(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("근육 추가 화면 반환 -> 정상, ModelAndView Check")
	public void addMuscleViewTestCheckMav() {
		//Given
		
		//When
		managerMuscleController.addMuscleView(mav);
		
		//Then
		then(mav).should(times(1)).setViewName("addMuscle");
	}
	
	/*
	 * public ModelAndView addMuscle(@ModelAttribute SaveMuscleDTO dto)
	 */
	
	@Test
	@DisplayName("근육 추가 처리 -> 추가하려는 Muscle의 Name이 중복된다면")
	public void addMuscleTestNameDuplicated() {
		//Given
		String existMuscleName = "exist";
		given(muscleService.existsByName(existMuscleName)).willReturn(true);
		
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(existMuscleName, TargetType.ARM.getKoreanName());
		//When, Then
		assertThrows(MuscleNameDuplicationFoundedException.class, () -> {
			managerMuscleController.addMuscle(saveMuscleDTO);
		});
	}
	
	@Test
	@DisplayName("근육 추가 처리 -> 추가하려는 Muscle의 category와 일치하는 TargetType 없으면")
	public void addMuscleTestTargetTypeNonExist() {
		//Given
		String newMuscleName = "newMuscle";
		given(muscleService.existsByName(newMuscleName)).willReturn(false);
		
		String nonExistTargetTypeKoreanNaem = "nonExist";
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(newMuscleName, nonExistTargetTypeKoreanNaem);
		
		//When, Then
		assertThrows(TargetTypeNotFoundedException.class, () -> {
			managerMuscleController.addMuscle(saveMuscleDTO);
		});
	}
	
	@Test
	@DisplayName("근육 추가 처리 -> 정상, MuscleService check")
	public void addMuscleTestCheckMuscleService() {
		//Given
		String newMuscleName = "newMuscle";
		given(muscleService.existsByName(newMuscleName)).willReturn(false);
		
		TargetType newMuscleCategory = TargetType.ARM;
		String newMuscleCategoryKoreanName = newMuscleCategory.getKoreanName();
		SaveMuscleDTO saveMuscleDTO = MuscleTest.getSaveMuscleDTOInstance(newMuscleName, newMuscleCategoryKoreanName);
		
		//When
		managerMuscleController.addMuscle(saveMuscleDTO);
		
		//Then
		then(muscleService).should(times(1)).save(any(Muscle.class));
	}
	
	/*
	 * public ModelAndView modifyMuscleView(@PathVariable("muscleId") Long muscleId, ModelAndView mav)
	 */
	
	@Test
	@DisplayName("Muscle 정보 수정 화면 반환 -> 해당 Id의 Muscle 없으면")
	public void modifyMuscleViewTestMuscleNonExist() {
		//Given
		Long nonExistMuscleId = Long.valueOf(35);
		given(muscleService.findById(nonExistMuscleId)).willReturn(null);
		
		//When, Then
		assertThrows(MuscleNotFoundedException.class, () -> {
			managerMuscleController.modifyMuscleView(nonExistMuscleId, mav);
		});
	}
	
	@Test
	@DisplayName("Muscle 정보 수정 화면 반환 -> 정상, ModelAndView check")
	public void modifyMuscleViewTestCheckMav() {
		//Given
		Long muscleId = Long.valueOf(78);
		Muscle muscle = MuscleTest.getMuscleInstance();
		given(muscleService.findById(muscleId)).willReturn(muscle);
		
		//When
		managerMuscleController.modifyMuscleView(muscleId, mav);
		
		//Then
		then(mav).should(times(1)).addObject("muscleId", muscleId);
		then(mav).should(times(1)).setViewName("modifyMuscle");
	}
}
