package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
import org.springframework.test.web.servlet.MvcResult;

import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.UserRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class UserRestControllerTest {
	
	private static final int INIT_USER_NUM = 5;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
	@BeforeEach
	void setup() {
		List<User> list = new ArrayList<>();
		for(int i=1;i<=INIT_USER_NUM;i++) {
			User user = User.builder()
					.email(i+"_user@test.com")
					.password("abcd1234!")
					.firstName(i+"번째")
					.lastName("사람")
					.nickName("User"+i)
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
	 * public String emailVerification(@RequestParam("email") String email)
	 */
	@Test
	@DisplayName("Email 중복확인 -> 정상")
	public void emailVerificationTest() throws Exception{
		
		MvcResult result = mockMvc.perform(get("/join/emailverification").param("email", "1_user@test.com")).andReturn();
				
		assertEquals("true",result.getResponse().getContentAsString());
				
		result = mockMvc.perform(get("/join/emailverification").param("email", "none@google.com")).andReturn();
				
		assertEquals("false",result.getResponse().getContentAsString());
	}
	
	/*
	 * public String nickNameVerification(@RequestParam("nickName")String nickName)
	 */
	
	@Test
	@DisplayName("Email 중복확인 -> 정상")
	public void nickNameVerificationTest() throws Exception{
		
		MvcResult result = mockMvc.perform(get("/join/nickNameverification").param("nickName", "User1")).andReturn();
				
		assertEquals("true",result.getResponse().getContentAsString());
				
		result = mockMvc.perform(get("/join/nickNameverification").param("nickName", "None")).andReturn();
				
		assertEquals("false",result.getResponse().getContentAsString());
	}
	
	
}
