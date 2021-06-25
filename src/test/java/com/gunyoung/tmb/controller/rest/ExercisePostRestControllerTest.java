package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.repos.UserRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class ExercisePostRestControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@Autowired
	PostLikeRepository postLikeRepository;
	
	protected MockHttpSession session;
	
	private User user;
	
	private Long userId;
	
	private ExercisePost exercisePost;
	
	private Long exercisePostId;
	
	@BeforeEach
	void setup() {
		session = new MockHttpSession();
		
		user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("test")
				.lastName("test")
				.nickName("test")
				.build();
	
		userRepository.save(user);
		userId = user.getId();
		
		session.setAttribute("LOGIN_USER_ID", userId);
		
		exercisePost = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		
		exercisePostRepository.save(exercisePost);
		
		exercisePostId = exercisePost.getId();
		
	}
	
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
		session.clearAttributes();
	}
	
	/*
	 *  public void addLikeToExercisePost(@RequestParam("post_id") Long postId)
	 */
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 좋아요 추가 요청 처리 -> 세션에 저장된 id의 User 없음")
	public void addLikeToExercisePostUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = userId + 1000;
		session.setAttribute("LOGIN_USER_ID", nonExistUserId);
		
		long postLikeNum = postLikeRepository.count();
		int userPostLikeNum = user.getPostLikes().size();
		int exercisePostPostLikeNum = exercisePost.getPostLikes().size();
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/addLike")
				.session(session))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(postLikeNum,postLikeRepository.count());
		assertEquals(userPostLikeNum,userRepository.findById(userId).get().getPostLikes().size());
		assertEquals(exercisePostPostLikeNum,exercisePostRepository.findById(exercisePostId).get().getPostLikes().size());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 좋아요 추가 요청 처리  -> 요청 파라미터에 해당하는 Exercise Post 없음")
	public void addLikeToExercisePostExercisePostNonExist() throws Exception {
		//Given
		Long nonExistExercisePostId = exercisePostId + 100;
		
		long postLikeNum = postLikeRepository.count();
		int userPostLikeNum = user.getPostLikes().size();
		int exercisePostPostLikeNum = exercisePost.getPostLikes().size();
		//When
		mockMvc.perform(post("/community/post/"+nonExistExercisePostId+"/addLike")
				.session(session))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(postLikeNum,postLikeRepository.count());
		assertEquals(userPostLikeNum,userRepository.findById(userId).get().getPostLikes().size());
		assertEquals(exercisePostPostLikeNum,exercisePostRepository.findById(exercisePostId).get().getPostLikes().size());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 좋아요 추가 요청 처리 -> 정상")
	public void addLikeToExercisePostTest() throws Exception {
		//Given
		long postLikeNum = postLikeRepository.count();
		int userPostLikeNum = user.getPostLikes().size();
		int exercisePostPostLikeNum = exercisePost.getPostLikes().size();
		
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/addLike")
				.session(session))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(postLikeNum+1,postLikeRepository.count());
		assertEquals(userPostLikeNum+1,userRepository.findById(userId).get().getPostLikes().size());
		assertEquals(exercisePostPostLikeNum+1,exercisePostRepository.findById(exercisePostId).get().getPostLikes().size());
	}
	
	
	/*
	 *  public void removeLikeToExercisePost(@PathVariable("post_id") Long postId)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 좋아요 취소 요청 처리 -> 해당 조건(User Id, Post Id)을 만족하는 PostLike 없음")
	public void removeLikeToExercisePost() throws Exception {
		//Given
		Long nonExistExercisePostId = exercisePostId + 100;
		
		long postLikeNum = postLikeRepository.count();
		
		//When
		
		mockMvc.perform(post("/community/post/"+ nonExistExercisePostId+"/removeLike")
				.session(session))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(postLikeNum, postLikeRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 좋아요 취소 요청 처리 -> 정상")
	public void removeLikeToExerciseTest() throws Exception {
		//Given
		
		PostLike postLike = PostLike.builder()
				.exercisePost(exercisePost)
				.user(user)
				.build();
		
		user.getPostLikes().add(postLike);
		exercisePost.getPostLikes().add(postLike);
		
		postLikeRepository.save(postLike);
		
		long postLikeNum = postLikeRepository.count();
		System.out.println(postLikeNum);
		int userPostLikeNum = user.getPostLikes().size();
		int exercisePostPostLikeNum = exercisePost.getPostLikes().size();
		
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/removeLike")
				.session(session))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(postLikeNum-1, postLikeRepository.count());
		assertEquals(userPostLikeNum-1,userRepository.findById(userId).get().getPostLikes().size());
		assertEquals(exercisePostPostLikeNum-1,exercisePostRepository.findById(exercisePostId).get().getPostLikes().size());
	}
}
