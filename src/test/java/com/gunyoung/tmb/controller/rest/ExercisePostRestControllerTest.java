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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.like.PostLike;
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
public class ExercisePostRestControllerTest {
	
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
	
	/*
	 *  public void addLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저가 댓글에 좋아요 추가 요청 처리 -> 해당 id의 유저 없음")
	public void addLikeToCommentUserNonExist() throws Exception {
		//Given
		Long nonExistUserId = Long.valueOf(1);
		
		for(User u : userRepository.findAll()) {
			nonExistUserId = Math.max(nonExistUserId, u.getId());
		}
		nonExistUserId++;
		
		session.setAttribute("LOGIN_USER_ID", nonExistUserId);
		
		Comment comment = Comment.builder()
				.contents("contents")
				.writerIp("172.0.0.1")
				.build();
		
		commentRepository.save(comment);
		
		Long commentId = comment.getId();
		
		int commentCommentLikeNum = comment.getCommentLikes().size();
		
		long commentLikeNum = commentLikeRepository.count();
		
		//When
		mockMvc.perform(post("/community/post/"+ exercisePostId +"/comment/addlike")
				.session(session)
				.param("commentId", commentId.toString()))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(commentCommentLikeNum,commentRepository.findById(commentId).get().getCommentLikes().size());
		
		assertEquals(commentLikeNum, commentLikeRepository.count());
		
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 댓글에 좋아요 추가 요청 처리 -> 해당 id의 comment 없음")
	public void addLikeToCommentCommentNonExist() throws Exception {
		//Given
		Long nonExistCommentId = Long.valueOf(1);
		
		for(Comment c: commentRepository.findAll()) {
			nonExistCommentId = Math.max(nonExistCommentId, c.getId());
		}
		
		nonExistCommentId++;
		
		int userCommentLikeNum = user.getCommentLikes().size();
		
		long commentLikeNum = commentLikeRepository.count();
		
		//When
		mockMvc.perform(post("/community/post/"+ exercisePostId +"/comment/addlike")
				.session(session)
				.param("commentId", nonExistCommentId.toString()))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(userCommentLikeNum, userRepository.findById(userId).get().getCommentLikes().size());
		assertEquals(commentLikeNum, commentLikeRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 댓글에 좋아요 추가 요청 처리  -> 정상")
	public void addLikeToCommentTest() throws Exception {
		//Given
		Comment comment = Comment.builder()
				.contents("contents")
				.writerIp("172.0.0.1")
				.build();
		
		commentRepository.save(comment);
		
		Long commentId = comment.getId();
		
		int userCommentLikeNum = user.getCommentLikes().size();
		int commentCommentLikeNum = comment.getCommentLikes().size();
		long commentLikeNum = commentLikeRepository.count();
		
		//When
		
		mockMvc.perform(post("/community/post/"+ exercisePostId +"/comment/addlike")
				.session(session)
				.param("commentId", commentId.toString()))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(userCommentLikeNum+1, userRepository.findById(userId).get().getCommentLikes().size());
		assertEquals(commentCommentLikeNum+1,commentRepository.findById(commentId).get().getCommentLikes().size());
		assertEquals(commentLikeNum+1, commentLikeRepository.count());
	}
	
	/*
	 *  public void removeLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저가 댓글에 좋아요 취소 요청 처리 -> 해당하는 좋아요 없음")
	public void removeLikeToCommentCommentLikeNonExist() throws Exception {
		//Given
		Long nonExistCommentId = Long.valueOf(1);
		
		for(Comment c: commentRepository.findAll()) {
			nonExistCommentId = Math.max(nonExistCommentId, c.getId());
		}
		
		nonExistCommentId ++;
		
		long commentLikeNum = commentLikeRepository.count();
		
		//When
		
		mockMvc.perform(post("/community/post/" + exercisePostId+ "/comment/removelike")
				.session(session)
				.param("commentId", nonExistCommentId.toString()))
		
		//Then
				.andExpect(status().isNoContent());
		
		assertEquals(commentLikeNum, commentLikeRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("유저가 댓글에 좋아요 취소 요청 처리  -> 정상")
	public void removeLikeToCommentCommentLikeTest() throws Exception {
		//Given
		Comment comment = Comment.builder()
				.contents("contents")
				.writerIp("172.0.0.1")
				.build();
		
		commentRepository.save(comment);
		
		CommentLike commentLike = CommentLike.builder()
				.comment(comment)
				.user(user)
				.build();
		
		user.getCommentLikes().add(commentLike);
		comment.getCommentLikes().add(commentLike);
		
		commentLikeRepository.save(commentLike);
		
		long commentLikeNum = commentLikeRepository.count();
		
		int userCommentLikeNum = user.getCommentLikes().size();
		int commentCommentLikeNum = comment.getCommentLikes().size();
		
		//When
		
		mockMvc.perform(post("/community/post/" + exercisePostId +"/comment/removelike")
				.session(session)
				.param("commentId", comment.getId().toString()))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(commentLikeNum-1, commentLikeRepository.count());
		assertEquals(userCommentLikeNum -1, userRepository.findById(userId).get().getCommentLikes().size());
		assertEquals(commentCommentLikeNum -1, commentRepository.findById(comment.getId()).get().getCommentLikes().size());
		
	}
}

