package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.response.MuscleForTableDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * {@link ManagerMuscleController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerMuscleControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MuscleRepository muscleRepository;
	
	/**
	 *  --------------- 테스트 진행과정에 있어 필요한 리소스 반환 메소드들 --------------------- 
	 */
	
	private Muscle getMuscleInstance() {
		Muscle muscle = Muscle.builder()
				.category(TargetType.ARM)
				.name("name")
				.build();
		
		return muscle;
	}
	
	private Long getNonExistMuscleId() {
		Long nonExistMuscleId = Long.valueOf(1);
		
		for(Muscle muscle : muscleRepository.findAll()) {
			nonExistMuscleId = Math.max(nonExistMuscleId, muscle.getId());
		}
		
		nonExistMuscleId++;
		return nonExistMuscleId;
	}
	
	private MultiValueMap<String, String> getAddMuscleDTOMap(String name, String category) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("name", name);
		map.add("category", category);
		return map;
	}
	
	private Map<String, Object> getResponseModel(MvcResult mvcResult) {
		return mvcResult.getModelAndView().getModel();
	}
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/manager/muscle",method = RequestMethod.GET)
	 * public ModelAndView muscleViewForManager(@RequestParam(value="page" , required=false,defaultValue="1") Integer page,@RequestParam(value="keyword",required=false) String keyword,ModelAndView mav) 
	 */
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 리스트 화면 반환 -> 정상")
	public void muscleViewForManagerTest() throws Exception {
		//Given
		int muscleNum = 10;
		
		List<Muscle> muscleList = new LinkedList<>();
		for(int i=1;i<=muscleNum; i++) {
			Muscle muscle = getMuscleInstance();
			muscleList.add(muscle);
		}
		muscleRepository.saveAll(muscleList);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/muscle"))
		
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		List<MuscleForTableDTO> resultList = (List<MuscleForTableDTO>) model.get("listObject");
		
		assertEquals(Math.min(muscleNum, PageUtil.MUSCLE_FOR_MANAGE_PAGE_SIZE),resultList.size());
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
	 * public ModelAndView addMuscle(@ModelAttribute AddMuscleDTO dto)
	 */
	
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("Muscle 추가 처리 -> 이름 중복")
	public void addMuscleDuplicated() throws Exception {
		//Given
		Muscle muscle = getMuscleInstance();
		muscleRepository.save(muscle);
		
		MultiValueMap<String, String> paramMap =  getAddMuscleDTOMap(muscle.getName(),TargetType.BACK.getKoreanName());
		
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
		MultiValueMap<String, String> paramMap =  getAddMuscleDTOMap("muscle",nonExistTargetTypeName);
		
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
		MultiValueMap<String, String> paramMap =  getAddMuscleDTOMap("muscle",TargetType.BACK.getKoreanName());
		
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
		Long nonExistMuscleId = getNonExistMuscleId();
		
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
		Muscle muscle = getMuscleInstance();
		muscleRepository.save(muscle);
		
		//When
		mockMvc.perform(get("/manager/muscle/modify/" + muscle.getId()))
		
		//Then
				.andExpect(status().isOk());
	}
}
