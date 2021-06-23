package com.gunyoung.tmb.controller.rest;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.repos.UserRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserExerciseRestControllerTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserExerciseRepository userExerciseRepository;
	
	@BeforeEach
	void setup() {
		User user = User.builder()
					.email("test@test.com")
					.password("abcd1234")
					.firstName("test")
					.lastName("test")
					.nickName("test")
					.build();
		
		userRepository.save(user);
	
		UserExercise ue = UserExercise.builder()
									.laps(1)
									.sets(1)
									.weight(100)
									.description("ad")
									.date(new Date())
									.user(user)
									.build();
		
		user.getUserExercises().add(ue);
		userExerciseRepository.save(ue);
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
		userExerciseRepository.deleteAll();
	}
	
	@Test
	@Transactional
	void test() {
		Long id = userRepository.findByEmail("test@test.com").get().getId();
		
		
		System.out.println(userExerciseRepository.
				findUserExercisesByUserIdAndDate(id,Calendar.getInstance()));
	}
	
}
