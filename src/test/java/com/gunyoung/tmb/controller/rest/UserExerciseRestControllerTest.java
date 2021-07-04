package com.gunyoung.tmb.controller.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class UserExerciseRestControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	protected MockHttpSession session;
	
	@BeforeEach
	void setup() {
		session = new MockHttpSession();
		User user = User.builder()
					.email("test@test.com")
					.password("abcd1234")
					.firstName("test")
					.lastName("test")
					.nickName("test")
					.build();
		
		userRepository.save(user);
		Long userId = user.getId();
		
		session.setAttribute("LOGIN_USER_ID", userId);
		
		Exercise e = Exercise.builder()
				.name("name")
				.movement("movement")
				.description("description")
				.target(TargetType.ARM)
				.caution("caution")
				.build();
		
		exerciseRepository.save(e);
	
		UserExercise ue = UserExercise.builder()
									.laps(1)
									.sets(1)
									.weight(100)
									.description("ad")
									.date(Calendar.getInstance())
									.user(user)
									.exercise(e)
									.build();
		
		user.getUserExercises().add(ue);
		userExerciseRepository.save(ue);
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
		userExerciseRepository.deleteAll();
		session.clearAttributes();
	}
	
	/*
	 *  public List<UserExercise> getExerciseRecords(@ModelAttribute("date")DateDTO date)
	 */
	@WithMockUser(username="test@test.com",roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("유저의 특정날짜 운동 기록 가져오는 컨트롤러 -> 정상")
	void getExerciseRecordsTest() throws Exception {
		//Given
		Calendar now = Calendar.getInstance();
		
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("year",String.valueOf(now.get(Calendar.YEAR)));
		paramMap.add("month", String.valueOf(now.get(Calendar.MONTH)));
		paramMap.add("date", String.valueOf(now.get(Calendar.DATE)));
		
		//When
		mockMvc.perform(get("/user/exercise/calendar/records")
				.session(session)
				.params(paramMap))
		
		//Then
				.andExpect(status().isOk());
	}
	
}
