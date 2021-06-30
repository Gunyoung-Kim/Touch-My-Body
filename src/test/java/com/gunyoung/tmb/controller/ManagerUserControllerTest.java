package com.gunyoung.tmb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class ManagerUserControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		
	}
	
	@AfterEach
	void tearDown() {
		
	}
	
	@WithMockUser(username="test@test.com", roles= {"ADMIN"})
	@Test
	public void test() throws Exception {
		mockMvc.perform(get("/manager/usermanage/1"));
	}
}
