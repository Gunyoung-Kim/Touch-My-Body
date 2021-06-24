package com.gunyoung.tmb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.reqeust.UserJoinDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class UserControllerTest {
	
	private static final int INIT_USER_NUM = 10;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
	@BeforeEach
	void setup() {
		List<User> list = new ArrayList<>();
		for(int i=1;i<=INIT_USER_NUM;i++) {
			User user = User.builder()
							.email("test"+i+"@test.com")
							.password("abcd1234!")
							.firstName("first"+i)
							.lastName("last"+i)
							.nickName("nickName" +i)
							.role(RoleType.USER)
							.build();
			list.add(user);
		}
		
		userRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}
	
	/*
	 *  public ModelAndView join(ModelAndView mav)
	 */
	
	@Test
	@DisplayName("유저 회원가입 -> 실패 (Email 중복)")
	public void joinEmailDuplicationFounded() throws Exception {
		//Given
		UserJoinDTO userJoinDTO = UserJoinDTO.builder()
										     .email("test1@test.com")
										     .password("abcd1234!")
										     .firstName("test")
										     .lastName("test")
										     .nickName("test")
										     .build();
		//When
		
		mockMvc.perform(post("/join")
					.params(getUserJoinParams(userJoinDTO)))
		//Then
					.andExpect(status().isConflict());
		
	}
	
	@Test
	@DisplayName("유저 회원가입 -> 실패 (NickName 중복)")
	public void joinNickNameDuplicationFounded() throws Exception {
		//Given
		UserJoinDTO userJoinDTO = UserJoinDTO.builder()
			     .email("test@test.com")
			     .password("abcd1234!")
			     .firstName("test")
			     .lastName("test")
			     .nickName("nickName1")
			     .build();
		//When
		
		mockMvc.perform(post("/join")
	 			.params(getUserJoinParams(userJoinDTO)))
		//Then
				.andExpect(status().isConflict());
		
	}
	
	@Test
	@Transactional
	@DisplayName("유저 회원가입 -> 정상")
	public void joinTest() throws Exception {
		//Given
		UserJoinDTO userJoinDTO = UserJoinDTO.builder()
			     .email("test@test.com")
			     .password("abcd1234!")
			     .firstName("test")
			     .lastName("test")
			     .nickName("test")
			     .build();
		long beforeNum = userRepository.count();
		
		//When
		mockMvc.perform(post("/join")
				.params(getUserJoinParams(userJoinDTO)))
		//Then
				.andExpect(redirectedUrl("/"));
		
		assertEquals(beforeNum+1,userRepository.count());
	}
	
	private MultiValueMap<String,String> getUserJoinParams(UserJoinDTO userJoinDTO) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", userJoinDTO.getEmail());
		map.add("password", userJoinDTO.getPassword());
		map.add("firstName", userJoinDTO.getFirstName());
		map.add("lastName", userJoinDTO.getLastName());
		map.add("nickName", userJoinDTO.getNickName());
		
		return map;
	}
	
}
