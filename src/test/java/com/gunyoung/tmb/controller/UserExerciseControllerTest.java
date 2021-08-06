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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserExeriseController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserExerciseControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private UserExerciseRepository userExerciseRepository;
	
	private User user;
	
	@BeforeEach
	void setup() {
		user = getUserInstance(RoleType.USER);
		userRepository.save(user);
	}
	
	@AfterEach 
	void tearDown() {
		userRepository.deleteAll();
	}
	
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
	
	private Long getNonExistUserId() {
		Long nonExistUserId = Long.valueOf(1);
		
		for(User u : userRepository.findAll()) {
			nonExistUserId = Math.max(nonExistUserId, u.getId());
		}
		nonExistUserId++;
		
		return nonExistUserId;
	}
	
	private MultiValueMap<String, String> getSaveUserExerciseDTOMap(String exerciseName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("laps", "1");
		map.add("sets", "1");
		map.add("weight", "1");
		map.add("description", "des");
		map.add("date", "2021-05-05");
		map.add("exerciseName", exerciseName);
		return map;
	}
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/user/exercise/calendar",method = RequestMethod.GET)
	 * public ModelAndView calendarView(ModelAndView mav)
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("유저의 운동 기록 캘린더 화면 반환 -> 정상")
	public void calendarViewTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/user/exercise/calendar"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/user/exercise/calendar/addrecord",method = RequestMethod.GET)
	 * public ModelAndView addUserExerciseView(ModelAndView mav)
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("운동 기록 추가 화면 반환 -> 정상")
	public void addUserExerciseViewTest() throws Exception {
		//Given
		
		//When
		mockMvc.perform(get("/user/exercise/calendar/addrecord"))
		
		//Then
				.andExpect(status().isOk());
	}
	
	/*
	 * @RequestMapping(value="/user/exercise/calendar/addrecord",method = RequestMethod.POST)
	 * @LoginIdSessionNotNull
	 * public ModelAndView addUserExercise(@ModelAttribute("formModel") SaveUserExerciseDTO formModel)
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("운동 기록 추가 처리 -> 접속된 세션의 Id의 User 없을 때")
	public void addUserExerciseUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = getNonExistUserId();
		
		Exercise exercise = getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		MultiValueMap<String, String> paramMap = getSaveUserExerciseDTOMap(exercise.getName());
		
		//When
		mockMvc.perform(post("/user/exercise/calendar/addrecord")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, nonExistUserId)
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("운동 기록 추가 처리 -> 해당 이름을 만족하는 Exercise 없을 때")
	public void addUserExerciseNonExist() throws Exception {
		//Given
		MultiValueMap<String, String> paramMap = getSaveUserExerciseDTOMap("nonExistExerciseName");
		
		//When
		mockMvc.perform(post("/user/exercise/calendar/addrecord")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("운동 기록 추가 처리 -> 정상")
	public void addUserExerciseTest() throws Exception {
		//Given
		Exercise exercise = getExerciseInstance("exercise",TargetType.ARM);
		exerciseRepository.save(exercise);
		
		MultiValueMap<String, String> paramMap = getSaveUserExerciseDTOMap(exercise.getName());
		
		//When
		mockMvc.perform(post("/user/exercise/calendar/addrecord")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.params(paramMap))
		
		//Then
				.andExpect(redirectedUrl("/user/exercise/calendar/addrecord"));
		
		assertEquals(1,userExerciseRepository.count());
	}
}
