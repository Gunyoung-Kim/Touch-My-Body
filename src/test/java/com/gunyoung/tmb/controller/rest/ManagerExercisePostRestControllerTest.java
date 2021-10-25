package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * {@link ManagerExercisePostRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
class ManagerExercisePostRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ExercisePostRepository exercisePostRepository;
	
	/*
	 * @RequestMapping(value="/manager/community/remove/post", method = RequestMethod.DELETE)
	 * void removeExercisePostByManager(@RequestParam("postId") Long postId)
	 */
	@WithMockUser(roles = {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("매니저의 게시글 삭제 -> 정상")
	void removeExercisePostByManagerTest() throws Exception {
		//Given
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(delete("/manager/community/remove/post")
				.param("postId", String.valueOf(ep.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(0, exercisePostRepository.findAll().size());
	}
}
