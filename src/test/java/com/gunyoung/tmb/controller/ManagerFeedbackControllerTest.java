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
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.FeedbackManageListDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * {@link ManagerFeedbackController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerFeedbackControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	/**
	 *  --------------- 테스트 진행과정에 있어 필요한 리소스 반환 메소드들 --------------------- 
	 */
	
	private User getUserInstance(RoleType role) {
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234!")
				.firstName("first")
				.lastName("last")
				.nickName("nickName")
				.role(role)
				.build();
		
		return user;
	}
	
	private Feedback getFeedbackInstance() {
		Feedback feedback = Feedback.builder()
				.content("content")
				.title("title")
				.build();
		
		return feedback;
	}
	
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
	
	private Long getNonExistExerciseId() {
		Long nonExistExerciseId = Long.valueOf(1);
		
		for(Exercise u : exerciseRepository.findAll()) {
			nonExistExerciseId = Math.max(nonExistExerciseId, u.getId());
		}
		nonExistExerciseId++;
		
		return nonExistExerciseId;
	}
	
	private Long getNonExistFeedbackId() {
		Long nonExistFeedbackId = Long.valueOf(1);
		
		for(Feedback u : feedbackRepository.findAll()) {
			nonExistFeedbackId = Math.max(nonExistFeedbackId, u.getId());
		}
		nonExistFeedbackId++;
		
		return nonExistFeedbackId;
	}
	
	private Map<String, Object> getResponseModel(MvcResult mvcResult) {
		return mvcResult.getModelAndView().getModel();
	}
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/manager/exercise/feedback/{exerciseId}" ,method= RequestMethod.GET) 
	 * public ModelAndView feedbackListView(@PathVariable("exerciseId") Long exerciseId,@RequestParam(value="page", defaultValue="1") int page
	 *		,ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 운동 정보에 대한 피드백들 리스트 화면 반환 -> 해당 ID의 Exercise 없을때")
	public void feedbackListViewNonExist() throws Exception {
		//Given
		Long nonExistExerciseId = getNonExistExerciseId();
		
		//When
		mockMvc.perform(get("/manager/exercise/feedback/" + nonExistExerciseId))
			
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 운동 정보에 대한 피드백들 리스트 화면 반환 -> 정상")
	public void feedbackListViewTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance("exercise", TargetType.ARM);
		exerciseRepository.save(exercise);
		
		int feedbackNum = 10;
		List<Feedback> feedbackList = new LinkedList<>();
		
		for(int i=1; i <= feedbackNum; i++) {
			Feedback feedback = getFeedbackInstance();
			feedback.setUser(user);
			feedback.setExercise(exercise);
			feedbackList.add(feedback);
		}
		
		feedbackRepository.saveAll(feedbackList);
		
		//When
		MvcResult result = mockMvc.perform(get("/manager/exercise/feedback/" + exercise.getId()))
		
		//then
				.andExpect(status().isOk())
				.andReturn();
		
		Map<String, Object> model = getResponseModel(result);
		
		@SuppressWarnings("unchecked")
		Page<FeedbackManageListDTO> listObject = (Page<FeedbackManageListDTO>) model.get("listObject");
		
		assertEquals(Math.min(feedbackNum, PageUtil.FEEDBACK_FOR_MANAGE_PAGE_SIZE),listObject.getContent().size());
	}
	
	/*
	 * @RequestMapping(value="/manager/exercise/feedback/detail/{feedbackId}" ,method = RequestMethod.GET) 
	 * public ModelAndView feedbackView(@PathVariable("feedbackId") Long feedbackId, ModelAndView mav)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 Feedback 상세화면 반환 -> 해당 ID의 Feedback 없을 때")
	public void feedbackViewNonExist() throws Exception {
		//Given
		Long nonExistFeedbackId = getNonExistFeedbackId();
		
		//When
		mockMvc.perform(get("/manager/exercise/feedback/detail/"+ nonExistFeedbackId))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 Feedback 상세화면 반환 -> 정상")
	public void feedbackViewTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Exercise exercise = getExerciseInstance("exercise", TargetType.ARM);
		exerciseRepository.save(exercise);
		
		Feedback feedback = getFeedbackInstance();
		feedback.setUser(user);
		feedback.setExercise(exercise);
		
		feedbackRepository.save(feedback);
		
		//When
		mockMvc.perform(get("/manager/exercise/feedback/detail/"+ feedback.getId()))
		
		//Then
				.andExpect(status().isOk());
	}
}
