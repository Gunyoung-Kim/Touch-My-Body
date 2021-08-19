package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.FeedbackTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link FeedbackController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	private User user;
	
	@BeforeEach
	void setup() {
		user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
	}
	
	@AfterEach 
	void tearDown() {
		userRepository.deleteAll();
	}
	
	/*
	 * @RequestMapping(value="/exercise/about/{exercise_id}/addfeedback",method=RequestMethod.GET)
	 * public ModelAndView addFeedbackView(@PathVariable("exercise_id") Long exerciseId,ModelAndView mav)
	 */
	
	@Test
	@Transactional
	@DisplayName("Feedback 추가 화면 반환 -> 해당 ID의 Exercise 없을 때")
	public void addFeedbackViewNonExist() throws Exception {
		//Given
		Long nonExistExerciseId = ExerciseTest.getNonExistExerciseId(exerciseRepository);
		
		//When
		mockMvc.perform(get("/exercise/about/" + nonExistExerciseId +"/addfeedback"))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@Test
	@Transactional
	@DisplayName("Feedback 추가 화면 반환 -> 정상")
	public void addFeedbackViewTest() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(get("/exercise/about/" + exercise.getId() + "/addfeedback"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/exercise/about/{exercise_id}/addfeedback",method=RequestMethod.POST)
	 * @LoginIdSessionNotNull
	 * public ModelAndView addFeedback(@PathVariable("exercise_id") Long exerciseId,@ModelAttribute SaveFeedbackDTO dto)
	 */
	
	@Test
	@Transactional
	@DisplayName("Feedback 추가 처리 -> 세션에 저장된 ID의 User 없을 때")
	public void addFeedbackUserNonExist() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(post("/exercise/about/" + exercise.getId() + "/addfeedback")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, UserTest.getNonExistUserId(userRepository))
				.params(FeedbackTest.getSaveFeedbackDTOMap()))
				
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(0,feedbackRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Feedback 추가 처리 -> 해당 ID에 해당하는 Exercise 없을 때")
	public void addFeedbackNonExist() throws Exception {
		//Given
		Long nonExistExerciseId = ExerciseTest.getNonExistExerciseId(exerciseRepository);
		//When
		mockMvc.perform(post("/exercise/about/" + nonExistExerciseId + "/addfeedback")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(FeedbackTest.getSaveFeedbackDTOMap()))
				
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(0,feedbackRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Feedback 추가 처리 -> 정상")
	public void addFeedbackTest() throws Exception {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		//When
		mockMvc.perform(post("/exercise/about/" + exercise.getId() + "/addfeedback")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(FeedbackTest.getSaveFeedbackDTOMap()))
				
		//Then
				.andExpect(redirectedUrl("/exercise/about/" + exercise.getId()+"/addfeedback"));
		
		assertEquals(1,feedbackRepository.count());
	}
}
