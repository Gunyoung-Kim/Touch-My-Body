package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseForInfoViewDTO;
import com.gunyoung.tmb.dto.response.ExerciseForTableDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * {@link ExerciseController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	/**
	 *  --------------- 테스트 진행과정에 있어 필요한 리소스 반환 메소드들 --------------------- 
	 */
	
	private Exercise getExerciseInstance(String name, TargetType target) {
		Exercise exercise = Exercise.builder()
				.name(name)
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(target)
				.build();
		
		return exercise;
	}
	
	private Map<String, Object> getResponseModel(MvcResult mvcResult) {
		return mvcResult.getModelAndView().getModel();
	}
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/exercise",method = RequestMethod.GET)
	 * public ModelAndView exerciseInfoMainView(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav)
	 */
	@Test
	@Transactional
	@DisplayName("운동 메인 화면 반환 -> 정상")
	public void exerciseInfoMainViewTest() throws Exception {
		//Given
		List<Exercise> exerciseList = new LinkedList<>();
		TargetType[] targetTypes = TargetType.values();
		for(TargetType tt : targetTypes) {
			exerciseList.add(getExerciseInstance(tt.getKoreanName(),tt));
		}
		
		exerciseRepository.saveAll(exerciseList);
		
		//When
		MvcResult result = mockMvc.perform(get("/exercise")
				.param("page", "1"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> modelMap = getResponseModel(result);
		@SuppressWarnings("unchecked")
		List<ExerciseForTableDTO> resultList = (List<ExerciseForTableDTO>) modelMap.get("listObject");
		
		assertEquals(Math.min(targetTypes.length,PageUtil.EXERCISE_INFO_TABLE_PAGE_SIZE), resultList.size());
	}
	
	/*
	 * @RequestMapping(value="/exercise/about/{exercise_id}", method = RequestMethod.GET)
	 * public ModelAndView exerciseInfoDetailView(@PathVariable("exercise_id") Long exerciseId,ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("특정 Exercise 상세 정보 화면 반환 -> 해당 ID의 Exercise 없을 때")
	public void exerciseInfoMainViewNonExist() throws Exception {
		//Given
		Exercise exercise = getExerciseInstance("exericse",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(get("/exercise/about/" + exercise.getId()+1))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@Test
	@Transactional
	@DisplayName("특정 Exercise 상세 정보 화면 반환 -> 정상")
	public void exerciseInfoDetailViewTest() throws Exception {
		//Given
		String name = "exercise";
		TargetType target = TargetType.ARM;
		Exercise exercise = getExerciseInstance(name,target);
		exerciseRepository.save(exercise);
		
		//When
		MvcResult result = mockMvc.perform(get("/exercise/about/" + exercise.getId()+1))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> modelMap = getResponseModel(result);
		
		ExerciseForInfoViewDTO dto = (ExerciseForInfoViewDTO) modelMap.get("exerciseInfo");
		assertEquals(name, dto.getName());
		assertEquals(target.getKoreanName(), dto.getTarget());
	}
	
	
}
