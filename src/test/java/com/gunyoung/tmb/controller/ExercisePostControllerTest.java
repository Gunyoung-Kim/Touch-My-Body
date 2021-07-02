package com.gunyoung.tmb.controller;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.repos.UserRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
public class ExercisePostControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@Autowired
	PostLikeRepository postLikeRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
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
				.role(RoleType.USER)
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
		exercisePostRepository.deleteAll();
		userRepository.deleteAll();
		session.clearAttributes();
	}
	
	/*
	 *  public void addCommentToExercisePost(@PathVariable("post_id") Long postId,@ModelAttribute AddCommentDTO dto,HttpServletRequest request)
	 */
	
	@Test
	@Transactional 
	@DisplayName("유저가 게시글에 댓글 추가 요청 처리 -> 세션에 저장된 user Id 없음")
	public void addCommentToExercisePostUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = userId + 1000;
		session.setAttribute("LOGIN_USER_ID", nonExistUserId);
		
		MultiValueMap<String, String> paramMap =  getAddCommentDTOParam();
		
		long commentNum = commentRepository.count();
		int userCommentNum = user.getComments().size();
		int exercisePostCommentNum = exercisePost.getComments().size();
		
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/addComment")
				.session(session)
				.params(paramMap))
	
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(commentNum,commentRepository.count());
		assertEquals(userCommentNum,userRepository.findById(userId).get().getComments().size());
		assertEquals(exercisePostCommentNum,exercisePostRepository.findById(exercisePostId).get().getComments().size());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 댓글 추가 요청 처리 -> 해당 id의 ExercisePost 없음")
	public void addCommentToExercisePostExercisePostNonExist()  throws Exception{
		//Given
		Long nonExistExercisePostId = exercisePostId + 100;
		
		MultiValueMap<String, String> paramMap =  getAddCommentDTOParam();
		
		long commentNum = commentRepository.count();
		int userCommentNum = user.getComments().size();
		int exercisePostCommentNum = exercisePost.getComments().size();
		
		//When
		mockMvc.perform(post("/community/post/"+nonExistExercisePostId+"/addComment")
				.session(session)
				.params(paramMap))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(commentNum,commentRepository.count());
		assertEquals(userCommentNum,userRepository.findById(userId).get().getComments().size());
		assertEquals(exercisePostCommentNum,exercisePostRepository.findById(exercisePostId).get().getComments().size());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 댓글 추가 요청 처리 -> 정상")
	public void addCommentToExercisePostTest() throws Exception {
		//Given
		MultiValueMap<String, String> paramMap =  getAddCommentDTOParam();
		
		long commentNum = commentRepository.count();
		int userCommentNum = user.getComments().size();
		int exercisePostCommentNum = exercisePost.getComments().size();
		
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/addComment")
				.session(session)
				.params(paramMap))
		
		//Then
			    .andExpect(status().isOk());
		
		assertEquals(commentNum+1,commentRepository.count());
		assertEquals(userCommentNum+1,userRepository.findById(userId).get().getComments().size());
		assertEquals(exercisePostCommentNum+1,exercisePostRepository.findById(exercisePostId).get().getComments().size());
	}
	
	private MultiValueMap<String, String> getAddCommentDTOParam() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("contents", "정말 좋은 댓글입니다...");
		map.add("isAnonymous","true");
		
		return map;
	}
	
	/*
	 *  public void removeCommentToExercisePost(@PathVariable("post_id") Long postId,@RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 댓글 삭제 요청 처리 -> 헤당 id의 댓글 없음")
	public void removeCommentToExercisePostCommentNonExist() throws Exception {
		//Given
		Long nonExistCommentId = Long.valueOf(100); 
		
		for(Comment c: commentRepository.findAll()) {
			nonExistCommentId = Math.max(nonExistCommentId, c.getId());
		}
		
		nonExistCommentId++;
		
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/removeComment")
				.session(session)
				.param("commentId", nonExistCommentId.toString()))
		
		//Then
			    .andExpect(status().isNoContent());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 댓글 삭제 요청 처리 -> 세션에 저장된 유저 id와 Comment 주인의 id 다름")
	public void removeCommentToExercisePostUserMotMatch() throws Exception {
		//Given
		User second = User.builder()
				.email("seconde@test.com")
				.password("abcd1234")
				.firstName("second")
				.lastName("second")
				.nickName("second")
				.build();
		
		userRepository.save(second);
		
		Comment comment = Comment.builder()
				.contents("contents")
				.isAnonymous(false)
				.writerIp("172.0.0.1")
				.user(second)
				.exercisePost(exercisePost)
				.build();
		
		second.getComments().add(comment);
		
		commentRepository.save(comment);
		Long commentId = comment.getId();
		
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/removeComment")
				.session(session)
				.param("commentId", commentId.toString()))
		//Then
				.andExpect(status().isForbidden());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 게시글에 댓글 삭제 요청 처리 -> 정상")
	public void removeCommentToExercisePostTest() throws Exception {
		//Given
		Comment comment = Comment.builder()
				.contents("contents")
				.isAnonymous(false)
				.writerIp("172.0.0.1")
				.user(user)
				.exercisePost(exercisePost)
				.build();
		
		user.getComments().add(comment);
		exercisePost.getComments().add(comment);
		
		commentRepository.save(comment);
		Long commentId = comment.getId();
		
		long commentNum = commentRepository.count();
		int userCommentNum = user.getComments().size();
		int exercisePostCommentNum = exercisePost.getComments().size();
		
		//When
		mockMvc.perform(post("/community/post/"+exercisePostId+"/removeComment")
				.session(session)
				.param("commentId", commentId.toString()))
		//Then
				.andExpect(status().isOk());
		
		assertEquals(commentNum-1,commentRepository.count());
		assertEquals(userCommentNum-1, userRepository.findById(userId).get().getComments().size());
		assertEquals(exercisePostCommentNum-1,exercisePostRepository.findById(exercisePostId).get().getComments().size());
	}
	
}
