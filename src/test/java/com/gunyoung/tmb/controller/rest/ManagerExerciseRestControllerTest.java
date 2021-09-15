package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.response.MuscleInfoBySortDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.MuscleTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * {@link ManagerExerciseRestController} 에 대한 테스트 클래스 
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerExerciseRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private MuscleRepository muscleRepository;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	/*
	 * @RequestMapping(value="/manager/exercise/add" ,method = RequestMethod.POST)
	 * public void addExercise(@ModelAttribute SaveExerciseDTO dto,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 추가 -> 이름 중복")
	public void addExerciseDuplicated() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		exerciseRepository.save(exercise);
		
		MultiValueMap<String,String> paramMap = ExerciseTest.getSaveExerciseDTOMap(exercise.getName());
		
		//When
		mockMvc.perform(post("/manager/exercise/add")
				.params(paramMap))
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(1,exerciseRepository.count());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 추가 -> 정상")
	public void addExerciseTest() throws Exception {
		//Given
		MultiValueMap<String,String> paramMap = ExerciseTest.getSaveExerciseDTOMap("exercise");
		
		//When
		mockMvc.perform(post("/manager/exercise/add")
				.params(paramMap))
		
		//Then
				.andExpect(status().isOk());
		assertEquals(1,exerciseRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/manager/exercise/modify/{exerciseId}",method=RequestMethod.PUT) 
	 * public void modifyExercise(@PathVariable("exerciseId") Long exerciseId, @ModelAttribute SaveExerciseDTO dto)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 수정 -> 해당 ID의 Exercise 없을때")
	public void modifyExerciseNonExist() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		String existingName = exercise.getName();
		
		exerciseRepository.save(exercise);
		
		MultiValueMap<String,String> paramMap = ExerciseTest.getSaveExerciseDTOMap("modifiedName");
		
		//When
		mockMvc.perform(put("/manager/exercise/modify/"+ exercise.getId()+1)
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(existingName,exerciseRepository.findById(exercise.getId()).get().getName());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 수정 -> 변경된 이름의 다른 운동이 존재할때")
	public void modifyExerciseAlreadyExist() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		String existingName = exercise.getName();
		
		exerciseRepository.save(exercise);
		
		Exercise anotherExercise = ExerciseTest.getExerciseInstance();
		String targetName = "second";
		anotherExercise.setName(targetName);
		
		exerciseRepository.save(anotherExercise);
		
		MultiValueMap<String,String> paramMap = ExerciseTest.getSaveExerciseDTOMap(existingName);
		
		//When
		mockMvc.perform(put("/manager/exercise/modify/"+ anotherExercise.getId())
				.params(paramMap))
		
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(targetName,exerciseRepository.findById(anotherExercise.getId()).get().getName());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 수정 -> 정상, description(중복되도 되는것)이 바뀔때")
	public void modifyExerciseDescriptionTest() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		String changedDescription = "modifiedDescription";
		
		exerciseRepository.save(exercise);
		
		MultiValueMap<String,String> paramMap = ExerciseTest.getSaveExerciseDTOMap(exercise.getName());
		paramMap.put("description", List.of("modifiedDescription"));
		
		//When
		mockMvc.perform(put("/manager/exercise/modify/"+ exercise.getId())
				.params(paramMap))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(changedDescription,exerciseRepository.findById(exercise.getId()).get().getDescription());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 수정 -> 정상, 이름(중복되면 안되는 것)이 바뀔때")
	public void modifyExerciseNameTest() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		String changedName = "modifiedName";
		
		exerciseRepository.save(exercise);
		
		MultiValueMap<String,String> paramMap = ExerciseTest.getSaveExerciseDTOMap(changedName);
		
		//When
		mockMvc.perform(put("/manager/exercise/modify/"+ exercise.getId())
				.params(paramMap))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(changedName,exerciseRepository.findById(exercise.getId()).get().getName());
	}
	
	/*
	 * @RequestMapping(value="/manager/exercise/remove" ,method = RequestMethod.DELETE) 
	 * public void deleteExercise(@RequestParam("exerciseId") Long exerciseId)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Exercise 삭제 -> 정상")
	public void deleteExerciseTest() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(delete("/manager/exercise/remove")
				.param("exerciseId", String.valueOf(exercise.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(0,exerciseRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/manager/exercise/getmuscles",method = RequestMethod.GET)
	 * public List<MuscleInfoBySortDTO> getMusclesSortByCategory()
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 종류별 분류 정보 요청 -> 정상")
	public void getMusclesSortByCategoryTest() throws Exception {
		//Given
		List<Muscle> muscleList = new LinkedList<>();
		TargetType[] targetTypes = TargetType.values();
		for(TargetType t :targetTypes) {
			Muscle m = MuscleTest.getMuscleInstance(t.getEnglishName(),t);
			muscleList.add(m);
		}
		
		muscleRepository.saveAll(muscleList);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/exercise/getmuscles")).andReturn();
		
		//Then
		String responseBodyAsString = result.getResponse().getContentAsString();
		
		List<MuscleInfoBySortDTO> resultList = objectMapper.readValue(responseBodyAsString, new TypeReference<List<MuscleInfoBySortDTO>>() {});
		
		assertEquals(resultList.size(), targetTypes.length);
	}
	
}
