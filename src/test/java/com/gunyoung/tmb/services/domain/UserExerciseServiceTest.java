package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;

/**
 * UserExerciseService 클래스에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserExerciseServiceTest {
	
	private static final int INIT_USER_EXERCISE_NUM = 30;
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseService userExerciseService;
	
	
	@BeforeEach
	void setup() {
		for(int i=1;i<=INIT_USER_EXERCISE_NUM;i++) {
			UserExercise userExercise = UserExercise.builder()
													.laps(i)
													.sets(i)
													.weight(i)
													.description(i+"번째 description")
													.date(Calendar.getInstance())
													.build();
			
			userExerciseRepository.save(userExercise);
		}
	}
	
	@AfterEach
	void tearDown() {
		userExerciseRepository.deleteAll();
	}
	
	/*
	 *   public UserExerciseService findById(Long id)
	 */
	@Test
	@DisplayName("Id로 해당 UserExercise 찾기 -> 해당 Id 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<UserExercise> list = userExerciseRepository.findAll();
		
		for(UserExercise ue: list) {
			maxId = Math.max(maxId, ue.getId());
		}
		
		//When
		UserExercise result = userExerciseService.findById(maxId+100);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@DisplayName("Id로 해당 UserExercise 찾기 ->  정상")
	public void findByIdTest() {
		//Given
		 Long existId = userExerciseRepository.findAll().get(0).getId();
		
		//When
		 UserExercise result = userExerciseService.findById(existId);
		 
		//Then
		 assertEquals(result != null,true);
	}
	
	/*
	 * public List<UserExercise> findByUserIdAndDate(Long userId, Calendar date)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저Id와 날짜로 운동 기록 찾기 ->정상")
	public void findByUserIdAndDateTest() {
		//Given
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.build();
	
		user = userRepository.save(user);
		Long userId = user.getId();
		
		List<UserExercise> list = userExerciseRepository.findAll();
		
		for(int i=0;i<INIT_USER_EXERCISE_NUM/3;i++) {
			UserExercise ue = list.get(i);
			
			ue.setUser(user);
			
			userExerciseRepository.save(ue);
		}
		
		//When
		List<UserExercise> resultList = userExerciseService.findByUserIdAndDate(userId, Calendar.getInstance());
		
		//Then
		assertEquals(resultList.size(),INIT_USER_EXERCISE_NUM/3);
	}
	
	/*
	 *   public UserExercise save(UserExercise userExercise)
	 */
	@Test
	@Transactional
	@DisplayName("UserExercise 정보 수정 -> 정상")
	public void mergeTest() {
		//Given
		UserExercise userExercise = userExerciseRepository.findAll().get(0);
		Long id = userExercise.getId();
		userExercise.setDescription("Changed");
		
		//When
		userExerciseService.save(userExercise);
		
		//Then
		Optional<UserExercise> result = userExerciseRepository.findById(id);
		assertEquals(result.get().getDescription(),"Changed");
	}
	
	@Test
	@Transactional
	@DisplayName("UserExercise 추가 -> 정상")
	public void saveTest() {
		//Given
		UserExercise userExercise = UserExercise.builder()
												.laps(1)
												.sets(1)
												.weight(1)
												.date(Calendar.getInstance())
												.description("new")
												.build();
		Long countBefore = userExerciseRepository.count();
		
		//When
		userExerciseService.save(userExercise);
		
		//Then
		assertEquals(countBefore+1,userExerciseRepository.count());
	}
	
	/*
	 *   public void delete(UserExercise userExercise)
	 */
	
	@Test
	@Transactional
	@DisplayName("UserExercise 삭제 -> 정상")
	public void deleteTest() {
		//Given
		UserExercise userExercise  = userExerciseRepository.findAll().get(0);
		Long id = userExercise.getId();
		
		//When
		userExerciseService.delete(userExercise);
		
		//Then
		Optional<UserExercise> result = userExerciseRepository.findById(id);
		
		assertEquals(result.isEmpty(),true);
	}
	
}
