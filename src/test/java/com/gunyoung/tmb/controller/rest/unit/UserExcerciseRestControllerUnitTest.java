package com.gunyoung.tmb.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.controller.rest.UserExcerciseRestController;
import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.DateDTO;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;
import com.gunyoung.tmb.dto.response.UserExerciseWithDateDTO;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.UserExerciseTest;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserExcerciseRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserExcerciseRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserExcerciseRestControllerUnitTest {
	
	@Mock
	HttpSession session;
	
	@Mock
	UserExerciseService userExerciseService;
	
	@InjectMocks
	UserExcerciseRestController userExcerciseRestController;
	
	/*
	 * public List<UserExerciseWithDateDTO> getExerciseRecords(@ModelAttribute DateDTO date) 
	 */
	
	@Test
	@DisplayName("접속자의 특정 날짜의 운동 기록들 반환 -> 정상")
	public void getExerciseRecordsTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		int givenUserExerciseNum = 10;
		List<UserExercise> userExerciseList = getUserExerciseList(givenUserExerciseNum);
		given(userExerciseService.findByUserIdAndDate(anyLong(), any(Calendar.class))).willReturn(userExerciseList);
		
		DateDTO dateDTO = getDateDTOInstance();
		
		//When
		List<UserExerciseWithDateDTO> result = userExcerciseRestController.getExerciseRecords(dateDTO);
		
		//Then
		assertEquals(givenUserExerciseNum, result.size());
	}
	
	private List<UserExercise> getUserExerciseList(int givenUserExerciseNum) {
		Exercise exercise = ExerciseTest.getExerciseInstance();
		List<UserExercise> userExerciseList = new ArrayList<>();
		for(int i=0;i<10;i++) {
			UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
			userExercise.setExercise(exercise);
			userExerciseList.add(userExercise);
		}
		return userExerciseList;
	}
	
	private DateDTO getDateDTOInstance() {
		int year = 1999;
		int month = Calendar.JANUARY;
		int date = 16;
		
		DateDTO dateDTO = DateDTO.builder()
				.year(year)
				.month(month)
				.date(date)
				.build();
		
		return dateDTO;
	}
	
	/*
	 * public List<UserExerciseIsDoneDTO> getIsDoneList(@RequestParam("year") int year, @RequestParam("month") int month)
	 */
	
	@Test
	@DisplayName("접속자의 특정 달에 각 일에 운동 했는지 여부 반환 -> 정상")
	public void getIsDoneListTest() {
		//Given
		Long loginIdInSession = Long.valueOf(1);
		given(session.getAttribute(SessionUtil.LOGIN_USER_ID)).willReturn(loginIdInSession);
		
		List<UserExerciseIsDoneDTO> isDoneList = new ArrayList<>();
		int year = 1999;
		int month = Calendar.JANUARY;
		given(userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(loginIdInSession, year, month)).willReturn(isDoneList);
		
		//When
		List<UserExerciseIsDoneDTO> result = userExcerciseRestController.getIsDoneList(year, month);
		
		//Then
		assertEquals(isDoneList, result);
	}
}
