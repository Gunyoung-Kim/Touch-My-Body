package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.UserExerciseTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * UserExerciseService 클래스에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class UserExerciseServiceTest {
	
	private static final Calendar DEFAULT_CALENDAR = new GregorianCalendar(1999,Calendar.JANUARY,16);
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseService userExerciseService;
	
	private UserExercise userExercise;
	
	@BeforeEach
	void setup() {
		userExercise = UserExerciseTest.getUserExerciseInstance(DEFAULT_CALENDAR);
		userExerciseRepository.save(userExercise);
	}
	
	@AfterEach
	void tearDown() {
		userExerciseRepository.deleteAll();
	}
	
	/*
	 *   UserExerciseService findById(Long id)
	 */
	@Test
	@DisplayName("Id로 해당 UserExercise 찾기 -> 해당 Id 존재하지 않음")
	void findByIdNonExist() {
		//Given
		long nonExistId = UserExerciseTest.getNonExistUserExerciseId(userExerciseRepository);
		
		//When
		UserExercise result = userExerciseService.findById(nonExistId+100);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Id로 해당 UserExercise 찾기 ->  정상")
	void findByIdTest() {
		//Given
		 Long existId = userExercise.getId();
		
		//When
		 UserExercise result = userExerciseService.findById(existId);
		 
		//Then
		 assertNotNull(result);
	}
	
	/*
	 * List<UserExercise> findByUserIdAndDate(Long userId, Calendar date)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저Id와 날짜로 운동 기록 찾기 ->정상")
	void findByUserIdAndDateTest() {
		//Given
		User user = UserTest.getUserInstance();
		user = userRepository.save(user);
		Long userId = user.getId();
		
		UserExerciseTest.addNewUserExercisesInDBByNum(10, userExerciseRepository);
		
		List<UserExercise> userExerciseList = userExerciseRepository.findAll();
		long givenUserExerciseNum = userExerciseList.size();
		
		for(UserExercise ue: userExerciseList) {
			ue.setUser(user);
			userExerciseRepository.save(ue);
		}
		
		//When
		List<UserExercise> resultList = userExerciseService.findByUserIdAndDate(userId, DEFAULT_CALENDAR);
		
		//Then
		assertEquals(givenUserExerciseNum, resultList.size());
	}
	
	/*
	 * List<UserExerciseIsDoneDTO> findIsDoneDTOByUserIdAndYearAndMonth(Long userId, int year,int month)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저 id, 년, 월로 날짜에 운동 했는지 여부 -> 정상, isDone 확인")
	void findIsDoneDTOByUserIdAndYearAndMonthCheckIsDone() {
		//Given
		User user = UserTest.getUserInstance();
		user = userRepository.save(user);
		Long userId = user.getId();
		
		userExercise.setUser(user);
		userExerciseRepository.save(userExercise);
		
		int existYear = DEFAULT_CALENDAR.get(Calendar.YEAR);
		int existMonth = DEFAULT_CALENDAR.get(Calendar.MONTH);
		int existDay = DEFAULT_CALENDAR.get(Calendar.DATE);
		
		//When
		List<UserExerciseIsDoneDTO> result = userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(userId,existYear,existMonth);
		
		//Then
		assertTrue(result.get(existDay-1).isDone());
	}
	
	@Test
	@Transactional
	@DisplayName("유저 id, 년, 월로 날짜에 운동 했는지 여부 -> 정상, date 확인")
	void findIsDoneDTOByUserIdAndYearAndMonthCheckDate() {
		//Given
		User user = UserTest.getUserInstance();
		user = userRepository.save(user);
		Long userId = user.getId();
		
		userExercise.setUser(user);
		userExerciseRepository.save(userExercise);
		
		int existYear = DEFAULT_CALENDAR.get(Calendar.YEAR);
		int existMonth = DEFAULT_CALENDAR.get(Calendar.MONTH);
		int existDay = DEFAULT_CALENDAR.get(Calendar.DATE);
		
		//When
		List<UserExerciseIsDoneDTO> result = userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(userId,existYear,existMonth);
		
		//Then
		assertEquals(result.get(existDay-1).getDate(),existDay);
	}
	
	/*
	 *   UserExercise save(UserExercise userExercise)
	 */
	
	@Test
	@Transactional
	@DisplayName("UserExercise 정보 수정 -> 정상, 변화 확인")
	void mergeTestCheckChange() {
		//Given
		String changeDescription = "Changed Description"; 
		Long userExerciseId = userExercise.getId();
		userExercise.setDescription(changeDescription);
		
		//When
		userExerciseService.save(userExercise);
		
		//Then
		Optional<UserExercise> result = userExerciseRepository.findById(userExerciseId);
		assertEquals(changeDescription, result.get().getDescription());
	}
	
	@Test
	@DisplayName("UserExercise 정보 수정 -> 정상, 개수 동일 확인")
	void mergeTestCheckCount() {
		//Given
		String changeDescription = "Changed Description"; 
		userExercise.setDescription(changeDescription);
		
		long givenUserExerciseNum = userExerciseRepository.count();
		
		//When
		userExerciseService.save(userExercise);
		
		//Then
		assertEquals(givenUserExerciseNum, userExerciseRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("UserExercise 추가 -> 정상")
	void saveTest() {
		//Given
		UserExercise userExercise = UserExerciseTest.getUserExerciseInstance(DEFAULT_CALENDAR);
		Long givenUserExerciseNum = userExerciseRepository.count();
		
		//When
		userExerciseService.save(userExercise);
		
		//Then
		assertEquals(givenUserExerciseNum + 1, userExerciseRepository.count());
	}
	
	/*
	 *  void delete(UserExercise userExercise)
	 */
	
	@Test
	@Transactional
	@DisplayName("UserExercise 삭제 -> 정상")
	void deleteTest() {
		//Given
		Long userExerciseId = userExercise.getId();
		
		//When
		userExerciseService.delete(userExercise);
		
		//Then
		Optional<UserExercise> result = userExerciseRepository.findById(userExerciseId);
		assertTrue(result.isEmpty());
	}
	
	/*
	 *  void deleteAllByExerciseId(Long exerciseId)
	 */
	
	@Test
	@Transactional
	@DisplayName("Exercise Id로 만족하는 UserExercise들 일괄 삭제 -> 정상")
	void deleteAllByExerciseIdTest() {
		//Given
		int newUserExercisesNum = 10;
		addUserExercises(newUserExercisesNum);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		setAllUserExercisesExercise(exercise);
		
		//When
		userExerciseService.deleteAllByExerciseId(exercise.getId());
		
		//Then
		assertEquals(0, userExerciseRepository.count());
	}
	
	private void addUserExercises(int userExerciseNum) {
		List<UserExercise> newUserExercises = new ArrayList<>();
		for(int i = 0 ; i < userExerciseNum; i++) {
			UserExercise userExercise = UserExerciseTest.getUserExerciseInstance();
			newUserExercises.add(userExercise);
		}
		userExerciseRepository.saveAll(newUserExercises);
	}
	
	private void setAllUserExercisesExercise(Exercise exercise) {
		List<UserExercise> userExercises = userExerciseRepository.findAll();
		for(UserExercise ue: userExercises) {
			ue.setExercise(exercise);
		}
		userExerciseRepository.saveAll(userExercises);
	}
}
