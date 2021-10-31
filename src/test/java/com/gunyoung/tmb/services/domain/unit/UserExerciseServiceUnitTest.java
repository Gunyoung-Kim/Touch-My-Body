package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;
import com.gunyoung.tmb.precondition.PreconditionViolationException;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.services.domain.user.UserExerciseServiceImpl;

/**
 * {@link UserExerciseServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserExerciseServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class UserExerciseServiceUnitTest {
	
	@Mock
	UserExerciseRepository userExerciseRepository;
	
	@InjectMocks 
	UserExerciseServiceImpl userExerciseService;
	
	private UserExercise userExercise;
	
	@BeforeEach
	void setup() {
		userExercise = UserExercise.builder()
				.build();
	}
	
	/*
	 * List<UserExercise> findByUserIdAndDate(Long userId, Calendar date)
	 */
	
	@Test
	@DisplayName("User Id, UserExercise date 로 UserExercise들 찾기 -> 정상")
	void findByUserIdAndDateTest() {
		//Given
		Long userId = Long.valueOf(1);
		Calendar date = new GregorianCalendar(1999, Calendar.JANUARY, 16);
		
		//When
		userExerciseService.findByUserIdAndDate(userId, date);
		
		//Then
		then(userExerciseRepository).should(times(1)).findUserExercisesByUserIdAndDate(userId, date);
	}
	
	/*
	 * List<UserExerciseIsDoneDTO> findIsDoneDTOByUserIdAndYearAndMonth(Long userId, int year,int month)
	 */
	
	@Test
	@DisplayName("특정 유저의 특정 년,월에 각 일마다 운동했는지 여부 반환 -> given year is negative")
	void findIsDoneDTOByUserIdAndYearAndMonthTestNegativeYear() {
		//Given
		Long userId = Long.valueOf(1);
		int year = -1;
		int month = Calendar.DECEMBER;
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(userId, year, month);
		});
	}
	
	@Test
	@DisplayName("특정 유저의 특정 년,월에 각 일마다 운동했는지 여부 반환 -> given month is negative")
	void findIsDoneDTOByUserIdAndYearAndMonthTestNegativeMonth() {
		//Given
		Long userId = Long.valueOf(1); 
		int year = 1999;
		int month = -1;
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(userId, year, month);
		});
	}
	
	@Test
	@DisplayName("특정 유저의 특정 년,월에 각 일마다 운동했는지 여부 반환 -> given month is over december")
	void findIsDoneDTOByUserIdAndYearAndMonthTestMonthOverDecember() {
		//Given
		Long userId = Long.valueOf(1);
		int year = 1999;
		int month = Calendar.DECEMBER + 1;
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(userId, year, month);
		});
	}
	
	@Test
	@DisplayName("특정 유저의 특정 년,월에 각 일마다 운동했는지 여부 반환하는 메소드 -> 정상")
	void findIsDoneDTOByUserIdAndYearAndMonthTest() {
		//Given
		Long userId = Long.valueOf(1);
		List<Calendar> calendarList = new ArrayList<>();
		int year = 1999;
		int month = Calendar.JANUARY;
		Calendar targetDate = new GregorianCalendar(year,month,16);
		calendarList.add(targetDate);
		
		Calendar startDate = new GregorianCalendar(year,month,1);
		Calendar endDate = new GregorianCalendar(year,month,31);
		
		given(userExerciseRepository.findUserExercisesIdForDayToDay(userId, startDate, endDate)).willReturn(calendarList);
		
		//When
		List<UserExerciseIsDoneDTO> result = userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(userId, year, month);
		
		//Then
		for(UserExerciseIsDoneDTO dto: result) {
			if(dto.getDate() != targetDate.get(Calendar.DATE)) {
				assertEquals(false, dto.isDone());
			} else {
				assertEquals(true, dto.isDone());
			}
		}
	}
	
	/*
	 * UserExercise save(UserExercise userExercise)
	 */
	
	@Test
	@DisplayName("UserExercise 생성 및 수정 -> 정상")
	void saveTest() {
		//Given
		given(userExerciseRepository.save(userExercise)).willReturn(userExercise);
		
		//When
		UserExercise result = userExerciseService.save(userExercise);
		
		//Then
		assertEquals(userExercise, result);
	}
	
	/*
	 * void delete(UserExercise userExercise)
	 */
	
	@Test
	@DisplayName("UserExercise 삭제 - 정상")
	void deleteTest() {
		//Given
		
		//When
		userExerciseService.delete(userExercise);
		
		//Then
		then(userExerciseRepository).should(times(1)).delete(userExercise);
	}
	
	/*
	 *  void deleteAllByExerciseId(Long exerciseId)
	 */
	
	@Test
	@DisplayName("User Id로 만족하는 UserExercise들 일괄 삭제 -> 정상")
	void deleteAllByUserIdTest() {
		//Given
		Long userId = Long.valueOf(25);
		
		//When
		userExerciseService.deleteAllByUserId(userId);
		
		//Then
		then(userExerciseRepository).should(times(1)).deleteAllByUserIdInQuery(userId);
	}
	
	/*
	 *  void deleteAllByExerciseId(Long exerciseId)
	 */
	
	@Test
	@DisplayName("Exercise Id로 만족하는 UserExercise들 일괄 삭제 -> 정상")
	void deleteAllByExerciseIdTest() {
		//Given
		Long exerciseId = Long.valueOf(25);
		
		//When
		userExerciseService.deleteAllByExerciseId(exerciseId);
		
		//Then
		then(userExerciseRepository).should(times(1)).deleteAllByExerciseIdInQuery(exerciseId);
	}
}
