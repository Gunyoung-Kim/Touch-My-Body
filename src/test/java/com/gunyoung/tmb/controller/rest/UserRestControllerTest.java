package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link UserRestController} 에 대한 테스트 클래스
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {
	
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
	
	
	/**
	 *  ---------------------------- 본 테스트 코드 ---------------------------------
	 */
	
	/*
	 * @RequestMapping(value="join/emailverification",method=RequestMethod.GET)
	 * public boolean emailVerification(@RequestParam("email") String email)
	 */
	
	@Test
	@Transactional
	@DisplayName("email 중복 여부 확인 -> true and false")
	public void emailVerificationTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		String existEmail = user.getEmail();
		String nonExistEmail = "nonExist@nonexist.com";
		
		//When 1 (true)
		MvcResult result = mockMvc.perform(get("/join/emailverification")
				.param("email", existEmail))
		
		//Then 1 (true)
				.andExpect(status().isOk()).andReturn();
		
		String responseBodyAsString = result.getResponse().getContentAsString();
		assertEquals("true",responseBodyAsString);
		
		//When 2 (false)
		result = mockMvc.perform(get("/join/emailverification")
				.param("email", nonExistEmail))
		
		//Then 2 (false)
				.andExpect(status().isOk()).andReturn();
		
		responseBodyAsString = result.getResponse().getContentAsString();
		assertEquals("false",responseBodyAsString);
	}
	
	/*
	 * @RequestMapping(value="join/nickNameverification",method=RequestMethod.GET)
	 * public boolean nickNameVerification(@RequestParam("nickName")String nickName)
	 */
	
	@Test
	@Transactional
	@DisplayName("nickName 중복 여부 확인 -> true and false")
	public void nickNameVerificationTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		String existNickName = user.getNickName();
		String nonExistNickName = "nonExist";
		
		//When 1 (true)
		MvcResult result = mockMvc.perform(get("/join/nickNameverification")
				.param("nickName", existNickName))
		
		//Then 1 (true)
				.andExpect(status().isOk()).andReturn();
		
		String responseBodyAsString = result.getResponse().getContentAsString();
		assertEquals("true",responseBodyAsString);
		
		//When 2 (false)
		result = mockMvc.perform(get("/join/nickNameverification")
				.param("nickName", nonExistNickName))
		
		//Then 2 (false)
				.andExpect(status().isOk()).andReturn();
		
		responseBodyAsString = result.getResponse().getContentAsString();
		assertEquals("false",responseBodyAsString);
	}
	
	/*
	 * @RequestMapping(value="/user/profile/mycomments/remove", method=RequestMethod.DELETE)
	 * public void removeMyComments(@RequestParam("commentId") Long commentId)
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("자신이 작성한 특정 댓글 삭제 -> 내 댓글 아님")
	public void removeMyCommentsNotMine() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		
		userRepository.save(user);
		
		Comment comment = getCommentInstance();
		commentRepository.save(comment);
		
		//When
		mockMvc.perform(delete("/user/profile/mycomments/remove")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("commentId", String.valueOf(comment.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(1, commentRepository.count());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("자신이 작성한 특정댓글 삭제 -> 정상")
	public void removeMyCommentsTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		
		userRepository.save(user);
		
		Comment comment = getCommentInstance();
		comment.setUser(user);
		commentRepository.save(comment);
		
		//When
		mockMvc.perform(delete("/user/profile/mycomments/remove")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("commentId", String.valueOf(comment.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(0, commentRepository.count());
	}
	
	/*
	 * @RequestMapping(value="/user/profile/myposts/remove",method=RequestMethod.DELETE) 
	 * public void removeMyPosts(@RequestParam("postId") Long postId)
	 */
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("자신이 작성한 특정 댓글 삭제 -> 내 댓글 아님")
	public void removeMyPostsNotMine() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		
		userRepository.save(user);
		
		ExercisePost exercisePost = getExercisePostInstance();
		
		exercisePostRepository.save(exercisePost);
		
		//When
		mockMvc.perform(delete("/user/profile/myposts/remove")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("postId", String.valueOf(exercisePost.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(1, exercisePostRepository.count());
	}
	
	@WithMockUser
	@Test
	@Transactional
	@DisplayName("자신이 작성한 특정댓글 삭제 -> 정상")
	public void removeMyPostsTest() throws Exception {
		//Given
		User user = getUserInstance(RoleType.USER);
		
		userRepository.save(user);
		
		ExercisePost exercisePost = getExercisePostInstance();
		exercisePost.setUser(user);
		exercisePostRepository.save(exercisePost);
		
		//When
		mockMvc.perform(delete("/user/profile/myposts/remove")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				.param("postId", String.valueOf(exercisePost.getId())))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(0, exercisePostRepository.count());
	}
}
