package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.response.ExerciseInfoBySortDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.tag.Integration;

/**
 * {@link ExerciseRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	/*
	 * @RequestMapping(value="/user/exercise/getexercises",method=RequestMethod.GET)
	 * public List<ExerciseInfoBySortDTO> getExercisesByNameAndTarget()
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("운동 종류별로 분류 후 반환하기 -> 정상")
	public void getExercisesByNameAndTargetTest() throws Exception {
		//Given
		List<Exercise> exerciseList = new LinkedList<>();
		TargetType[] targetTypes = TargetType.values();
		for(TargetType tt : targetTypes) {
			exerciseList.add(ExerciseTest.getExerciseInstance(tt.getKoreanName(),tt));
		}
		
		exerciseRepository.saveAll(exerciseList);
		
		//When
		MvcResult result = mockMvc.perform(get("/user/exercise/getexercises"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBodyAsString = result.getResponse().getContentAsString();
		List<ExerciseInfoBySortDTO> resultList = objectMapper.readValue(responseBodyAsString, new TypeReference<List<ExerciseInfoBySortDTO>>() {});
		
		assertEquals(targetTypes.length,resultList.size());
		
		for(ExerciseInfoBySortDTO dto: resultList) {
			assertEquals(1,dto.getExerciseNames().size());
		}
	}
}
