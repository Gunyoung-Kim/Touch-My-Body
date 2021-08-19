package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.util.MuscleTest;

/**
 * {@link ManagerMuscleRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerMuscleRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MuscleRepository muscleRepository;
	
	/*
	 * @RequestMapping(value="/manager/muscle/modify/{muscleId}", method = RequestMethod.PUT)
	 * public void modifyMuscle(@PathVariable("muscleId") Long muscleId, @ModelAttribute SaveMuscleDTO dto)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 수정 -> 해당 ID의 Muscle 없을 때")
	public void modifyMuscleNonExist() throws Exception {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		String existingName = muscle.getName();
		
		muscleRepository.save(muscle);
		
		MultiValueMap<String,String> paramMap = MuscleTest.getSaveMuscleDTOMap("changedName",muscle.getCategory());
		
		//When
		mockMvc.perform(put("/manager/muscle/modify/"+muscle.getId()+1)
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(existingName,muscleRepository.findById(muscle.getId()).get().getName());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 수정 -> 수정된 이름의 다른 Muscle 이미 존재")
	public void modifyMuscleAlreadyExist() throws Exception {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		String existingName = muscle.getName();
		muscleRepository.save(muscle);
		
		Muscle targetMuscle = MuscleTest.getMuscleInstance();
		String targetMuscleName = "second";
		targetMuscle.setName(targetMuscleName);
		muscleRepository.save(targetMuscle);
		
		MultiValueMap<String,String> paramMap = MuscleTest.getSaveMuscleDTOMap(existingName,targetMuscle.getCategory());
		
		//When
		mockMvc.perform(put("/manager/muscle/modify/" + targetMuscle.getId())
				.params(paramMap))
		
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(targetMuscleName,muscleRepository.findById(targetMuscle.getId()).get().getName());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 수정 -> 정상")
	public void modifyMuscleTest() throws Exception {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		muscleRepository.save(muscle);
		
		String changedName = "changedName";
		
		MultiValueMap<String,String> paramMap = MuscleTest.getSaveMuscleDTOMap(changedName,muscle.getCategory());
		
		//When
		mockMvc.perform(put("/manager/muscle/modify/"+muscle.getId())
				.params(paramMap))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(changedName,muscleRepository.findById(muscle.getId()).get().getName());
	}
	
	/*
	 * @RequestMapping(value="/manager/muscle/remove" ,method = RequestMethod.DELETE)
	 * public void removeMuscle(@RequestParam("muscleId") Long muscleId)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 삭제 -> 정상")
	public void removeMuscleTest() throws Exception {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		muscleRepository.save(muscle);
		
		//When
		mockMvc.perform(delete("/manager/muscle/remove")
				.param("muscleId", String.valueOf(muscle.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(0,muscleRepository.count());
	}
}
