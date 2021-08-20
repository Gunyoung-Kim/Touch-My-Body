package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.util.ControllerTest;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * {@link ManagerExerciseController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerExerciseControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	/*
	 * @RequestMapping(value="/manager/exercise",method= RequestMethod.GET)
	 * public ModelAndView exerciseViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 리스트 화면 반환 -> 정상, 키워드 없음")
	public void exerciseViewForManagerTestNoKeyword() throws Exception {
		//Given
		int givenExerciseNum = 10;
		ExerciseTest.addNewExercisesInDBByNum(givenExerciseNum, exerciseRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/exercise"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<ExerciseForTableDTO> listObject = (List<ExerciseForTableDTO>) model.get("listObject");
		
		assertEquals(Math.min(PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE, givenExerciseNum), listObject.size());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 리스트 화면 반환 -> 정상, 어느 Muscle도 만족하지 않는 키워드")
	public void exerciseViewForManagerTestKeywordForNothing() throws Exception {
		//Given
		String keywordForNothing = "nothing!!";
		
		int givenExerciseNum = 10;
		ExerciseTest.addNewExercisesInDBByNum(givenExerciseNum, exerciseRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/exercise")
				.param("keyword", keywordForNothing))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<ExerciseForTableDTO> listObject = (List<ExerciseForTableDTO>) model.get("listObject");
		
		assertEquals(0, listObject.size());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 리스트 화면 반환 -> 정상, 모든 Muscle이 만족하는 키워드")
	public void exerciseViewForManagerTest() throws Exception {
		//Given
		String keywordForAllExercise = ExerciseTest.DEFAULT_NAME;
		int givenExerciseNum = 10;
		ExerciseTest.addNewExercisesInDBByNum(givenExerciseNum, exerciseRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/exercise")
				.param("keyword", keywordForAllExercise))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<ExerciseForTableDTO> listObject = (List<ExerciseForTableDTO>) model.get("listObject");
		
		assertEquals(Math.min(PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE, givenExerciseNum), listObject.size());
	}
	
	/*
	 * @RequestMapping(value="/manager/exercise/add",method = RequestMethod.GET)
	 * public ModelAndView addExerciseView(ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 추가 화면 반환 -> 정상")
	public void addExerciseViewTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/manager/exercise/add"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/manager/exercise/modify/{exerciseId}", method = RequestMethod.GET) 
	 * public ModelAndView modifyExerciseView(@PathVariable("exerciseId") Long exerciseId, ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 수정 화면 반환 -> 해당 ID의 Exercise 없을 때")
	public void modifyExerciseViewNonExist() throws Exception {
		//Given
		Long nonExistExerciseId = ExerciseTest.getNonExistExerciseId(exerciseRepository);
		
		//When
		mockMvc.perform(get("/manager/exercise/modify/" + nonExistExerciseId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 수정 화면 반환 -> 정상")
	public void modifyExerciseViewTest() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(get("/manager/exercise/modify/" + exercise.getId()))
		
		//Then
				.andExpect(status().isOk());
	}
}
