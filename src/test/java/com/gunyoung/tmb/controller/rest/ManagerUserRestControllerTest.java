package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;

/**
 * {@link ManagerUserRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ManagerUserRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ExercisePostRepository exercisePostRepository;
	
	/**
	 *  --------------- 테스트 진행과정에 있어 필요한 리소스 반환 메소드들 --------------------- 
	 */
	
	private User getUserInstance(RoleType role) {
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234!")
				.firstName("first")
				.lastName("last")
				.nickName("nickName")
				.role(role)
				.build();
		
		return user;
	}
	
	private Comment getCommentInstance() {
		Comment comment = Comment.builder()
				.contents("contents")
				.isAnonymous(false)
				.writerIp("127.0.0.1")
				.build();
		return comment;
	}
	
	private ExercisePost getExercisePostInstance() {
		ExercisePost ep = ExercisePost.builder()
				.contents("contents")
				.title("title")
				.build();
		return ep;
	}
	
	private MultiValueMap<String,String> getUserProfileForManagerDTOMap(RoleType role) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("role", role.toString());
		return map;
	}
	
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="/manager/usermanage/{user_id}", method = RequestMethod.PUT)
	 * public void manageUserProfile(@PathVariable("user_id") Long userId,@ModelAttribute UserProfileForManagerDTO dto)
	 */
	
	@WithMockUser(roles= {"ADMIN"})
	@Test
	@Transactional
	@DisplayName("유저의 정보(권한) 수정 -> 해당 ID의 USER 없을 때")
	public void manageUserProfileNonExist() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		MultiValueMap<String,String> paramMap = getUserProfileForManagerDTOMap(RoleType.MANAGER);
		
		//When
		mockMvc.perform(put("/manager/usermanage/" + user.getId()+1)
				.params(paramMap))
		
		//Then
		
				.andExpect(status().isNoContent());
		
		assertEquals(RoleType.USER, userRepository.findById(user.getId()).get().getRole());
	}
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("유저의 정보(권한) 수정 -> 접속자가 대상 User보다 권한이 낮을 때")
	public void manageUserProfileAccessDenied() throws Exception {
		//Given
		User user = getUserInstance(RoleType.ADMIN);
		userRepository.save(user);
		
		MultiValueMap<String,String> paramMap = getUserProfileForManagerDTOMap(RoleType.MANAGER);
		
		//When
		mockMvc.perform(put("/manager/usermanage/" + user.getId())
				.params(paramMap))
		
		//Then
		
				.andExpect(status().isBadRequest());
		assertEquals(RoleType.ADMIN,userRepository.findById(user.getId()).get().getRole());
	}
	
	@WithMockUser(roles= {"ADMIN"})
	@Test
	@Transactional
	@DisplayName("유저의 정보(권한) 수정 -> 정상")
	public void manageUserProfileTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		MultiValueMap<String,String> paramMap = getUserProfileForManagerDTOMap(RoleType.MANAGER);
		
		//When
		mockMvc.perform(put("/manager/usermanage/" + user.getId())
				.params(paramMap))
		
		//Then
		
				.andExpect(status().isOk());
		assertEquals(RoleType.MANAGER,userRepository.findById(user.getId()).get().getRole());
	}
	
	/*
	 * @RequestMapping(value="/manager/usermanage/{user_id}/comments/remove", method = RequestMethod.DELETE)
	 * public void removeCommentByManager(@PathVariable("user_id") Long userId,@RequestParam("comment_id") Long commentId)
	 */
	
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 댓글 삭제하기 -> 정상")
	public void removeCommentByManagerTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		Comment comment = getCommentInstance();
		comment.setUser(user);
		commentRepository.save(comment);
		
		//When
		mockMvc.perform(delete("/manager/usermanage/" + user.getId() + "/comments/remove")
				.param("comment_id", String.valueOf(comment.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(0, commentRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/manager/usermanage/{user_id}/posts/remove",method = RequestMethod.DELETE) 
	 * public void removePostByManager(@PathVariable("user_id") Long userId,@RequestParam("post_id") Long postId)
	 */
	@WithMockUser(roles= {"MANAGER"})
	@Test
	@Transactional
	@DisplayName("특정 유저의 게시글 삭제 -> 정상")
	public void removePostByManager() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		ExercisePost ep = getExercisePostInstance();
		ep.setUser(user);
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(delete("/manager/usermanage/" + user.getId() +"/posts/remove")
				.param("post_id", String.valueOf(ep.getId())))
		
		//Then
				.andExpect(status().isOk());
		assertEquals(0, exercisePostRepository.count());
	}
	
	
	
}
