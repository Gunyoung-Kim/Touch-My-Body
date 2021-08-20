package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;
import com.gunyoung.tmb.dto.response.UserExerciseWithDateDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.UserExerciseTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserExerciseRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserExerciseRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserExerciseRepository userExerciseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	/*
	 * @RequestMapping(value="/user/exercise/calendar/records",method=RequestMethod.GET)
	 * @LoginIdSessionNotNull
	 * public List<UserExerciseWithDateDTO> getExerciseRecords(@ModelAttribute DateDTO date)
	 */
	
	@WithMockUser(roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("접속자의 특정 날짜 운동 기록 반환 -> 정상")
	public void getExerciseRecordsTest() throws Exception {
		//Given
		int year = 1999;
		int month = Calendar.JANUARY;
		int date = 16;
		
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		UserExercise ue = UserExerciseTest.getUserExerciseInstance(new GregorianCalendar(year,month,date));
		ue.setUser(user);
		ue.setExercise(exercise);
		
		userExerciseRepository.save(ue);
		
		//When
		MvcResult result = mockMvc.perform(get("/user/exercise/calendar/records")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("year", String.valueOf(year))
				.param("month", String.valueOf(month))
				.param("date", String.valueOf(date)))
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBodyAsString = result.getResponse().getContentAsString();
		List<UserExerciseWithDateDTO> resultList = objectMapper.readValue(responseBodyAsString, new TypeReference<List<UserExerciseWithDateDTO>>() {});
		assertEquals(1,resultList.size());
	}
	
	/*
	 * @RequestMapping(value="/user/exercise/calendar/isdone",method=RequestMethod.GET)
	 * @LoginIdSessionNotNull
	 * public List<UserExerciseIsDoneDTO> getIsDoneList(@RequestParam("year") int year, @RequestParam("month") int month)
	 */
	@WithMockUser(roles= {"USER"})
	@Test
	@Transactional
	@DisplayName("접속자의 특정 년월 운동 여부 반환 -> 정상")
	public void getIsDoneListTest() throws Exception {
		//Given
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		int year = 1999;
		int month = Calendar.JANUARY;
		int startDay = 2;
		int endDay = 15;
		
		for(int i=startDay;i<=endDay;i++) {
			Exercise exercise = ExerciseTest.getExerciseInstance();
			exerciseRepository.save(exercise);
			
			UserExercise ue = UserExerciseTest.getUserExerciseInstance(new GregorianCalendar(year,Calendar.JANUARY,i));
			ue.setUser(user);
			ue.setExercise(exercise);
			
			userExerciseRepository.save(ue);
		}
		
		//When
		MvcResult result = mockMvc.perform(get("/user/exercise/calendar/isdone")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("year", String.valueOf(year))
				.param("month", String.valueOf(month)))
		//Then
		
				.andExpect(status().isOk())
				.andReturn();
		
		String responseBodyAsString = result.getResponse().getContentAsString();
		List<UserExerciseIsDoneDTO> resultList = objectMapper.readValue(responseBodyAsString, new TypeReference<List<UserExerciseIsDoneDTO>>() {});
		
		for(UserExerciseIsDoneDTO response : resultList) {
			int date = response.getDate();
			
			if(date >= startDay && date <= endDay) {
				assertEquals(true,response.isDone());
			} else {
				assertEquals(false,response.isDone());
			}
		}
	}
}
