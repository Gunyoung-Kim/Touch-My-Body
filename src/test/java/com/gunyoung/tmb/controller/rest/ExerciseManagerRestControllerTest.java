package com.gunyoung.tmb.controller.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.MuscleRepository;
import com.gunyoung.tmb.repos.UserRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class ExerciseManagerRestControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MuscleRepository muscleRepository;
	
	protected MockHttpSession session;
	
	private User user;
	
	private Long userId;

	
	@BeforeEach
	void setup() {
		session = new MockHttpSession();
		
		user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.role(RoleType.MANAGER)
				.build();
	
		userRepository.save(user);
		userId = user.getId();
		
		session.setAttribute("LOGIN_USER_ID", userId);
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
		session.clearAttributes();
	}
	
	/*
	 * 	public List<MuscleInfoBySortDTO> getMusclesSortByCategory()
	 */
	@WithMockUser(username="test@test.com",roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("근육 종류별로 분류하여 전송 -> 성공")
	public void getMusclesSortByCategoryTest() throws Exception {
		//Given
		List<Muscle> list = new ArrayList<>();
		for(int i=1;i<=2;i++) {
			Muscle muscle = Muscle.builder()
									 .name("name" +i)
									 .category(TargetType.ARM)
									 .build();
									 
			list.add(muscle);
		}
		Muscle muscle = Muscle.builder()
				 .name("name3")
				 .category(TargetType.BACK)
				 .build();
		list.add(muscle);
		muscleRepository.saveAll(list);
		
		//When
		mockMvc.perform(get("/manager/exercise/getmuscles"))
		//Then
				.andExpect(status().isOk())
				.andReturn();
		
	}
	
}
