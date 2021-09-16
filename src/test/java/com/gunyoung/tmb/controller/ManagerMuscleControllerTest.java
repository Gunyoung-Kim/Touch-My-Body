package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.response.MuscleForTableDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.testutil.ControllerTest;
import com.gunyoung.tmb.testutil.MuscleTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * {@link ManagerMuscleController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerMuscleControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MuscleRepository muscleRepository;
	
	/*
	 * @RequestMapping(value="/manager/muscle",method = RequestMethod.GET)
	 * public ModelAndView muscleViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav) 
	 */
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 리스트 화면 반환 -> 정상, 키워드 없음")
	public void muscleViewForManagerTestNoKeyword() throws Exception {
		//Given
		int givenMuscleNum = 10;
		MuscleTest.addNewMusclesInDBByNum(givenMuscleNum, muscleRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/muscle"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<MuscleForTableDTO> resultList = (List<MuscleForTableDTO>) model.get("listObject");
		
		assertEquals(Math.min(givenMuscleNum, PageSize.MUSCLE_FOR_MANAGE_PAGE_SIZE.getSize()),resultList.size());
	}
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 리스트 화면 반환 -> 정상, 모든 Muscle이 만족하지 않는 키워드")
	public void muscleViewForManagerTestKeywordForNothing() throws Exception {
		//Given
		String keywordForNothing = "noting!!";
		int givenMuscleNum = 10;
		MuscleTest.addNewMusclesInDBByNum(givenMuscleNum, muscleRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/muscle")
				.param("keyword", keywordForNothing))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<MuscleForTableDTO> resultList = (List<MuscleForTableDTO>) model.get("listObject");
		
		assertEquals(0, resultList.size());
	}
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 리스트 화면 반환 -> 정상, 모든 Muscle이 만족하는 키워드")
	public void muscleViewForManagerTestKeywordForAll() throws Exception {
		//Given
		String keywordForAllMuscle = MuscleTest.DEFAULT_NAME;
		int givenMuscleNum = 10;
		MuscleTest.addNewMusclesInDBByNum(givenMuscleNum, muscleRepository);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/muscle")
				.param("keyword", keywordForAllMuscle))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = ControllerTest.getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<MuscleForTableDTO> resultList = (List<MuscleForTableDTO>) model.get("listObject");
		
		assertEquals(Math.min(givenMuscleNum, PageSize.MUSCLE_FOR_MANAGE_PAGE_SIZE.getSize()),resultList.size());
	}
	
	/*
	 * @RequestMapping(value="/manager/muscle/add" , method= RequestMethod.GET)
	 * public ModelAndView addMuscleView(ModelAndView mav)
	 */
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 추가 화면 반환 -> 정상")
	public void addMuscleViewTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/manager/muscle/add"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/manager/muscle/add" ,method=RequestMethod.POST)
	 * public ModelAndView addMuscle(@ModelAttribute SaveMuscleDTO dto)
	 */
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 추가 처리 -> 이름 중복")
	public void addMuscleDuplicated() throws Exception {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		muscleRepository.save(muscle);
		
		MultiValueMap<String, String> paramMap =  MuscleTest.getSaveMuscleDTOMap(muscle.getName(),TargetType.BACK.getKoreanName());
		
		//When
		mockMvc.perform(post("/manager/muscle/add")
				.params(paramMap))
		
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(1, muscleRepository.count());
	}
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 추가 처리 -> TargetType 오류")
	public void addMuscleTargetTypeNonExist() throws Exception {
		//Given
		String nonExistTargetTypeName = "nonExist";
		MultiValueMap<String, String> paramMap =  MuscleTest.getSaveMuscleDTOMap("muscle",nonExistTargetTypeName);
		
		//When
		mockMvc.perform(post("/manager/muscle/add")
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(0, muscleRepository.count());
	}
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 추가 처리 -> 정상")
	public void addMuscleTest() throws Exception {
		//Given
		MultiValueMap<String, String> paramMap =  MuscleTest.getSaveMuscleDTOMap("muscle",TargetType.BACK.getKoreanName());
		
		//When
		mockMvc.perform(post("/manager/muscle/add")
				.params(paramMap))
				
		//Then
				.andExpect(redirectedUrl("/manager/muscle"));
		
		assertEquals(1, muscleRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/manager/muscle/modify/{muscleId}", method = RequestMethod.GET)
	 * public ModelAndView modifyMuscleView(@PathVariable("muscleId") Long muscleId, ModelAndView mav)
	 */
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 수정 화면 반환 -> 해당 ID의 Muscle 없을 때")
	public void modifyMuscleViewNonExist() throws Exception {
		//Given
		Long nonExistMuscleId = MuscleTest.getNonExistMuscleId(muscleRepository);
		
		//When
		mockMvc.perform(get("/manager/muscle/modify/" + nonExistMuscleId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 수정 화면 반환 -> 정상")
	public void modifyMuscleViewTest() throws Exception {
		//Given
		Muscle muscle = MuscleTest.getMuscleInstance();
		muscleRepository.save(muscle);
		
		//When
		mockMvc.perform(get("/manager/muscle/modify/" + muscle.getId()))
		
		//Then
				.andExpect(status().isOk());
	}
}
