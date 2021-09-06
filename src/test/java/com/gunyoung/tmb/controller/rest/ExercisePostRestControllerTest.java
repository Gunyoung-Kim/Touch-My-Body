package com.gunyoung.tmb.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
import com.gunyoung.tmb.util.CommentLikeTest;
import com.gunyoung.tmb.util.CommentTest;
import com.gunyoung.tmb.util.ExercisePostTest;
import com.gunyoung.tmb.util.PostLikeTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.util.tag.Integration;
import com.gunyoung.tmb.utils.SessionUtil;

/**
 * {@link ExercisePostRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 프레젠테이션 계층 - 서비스 계층 - 영속성 계층 <br>
 * MockMvc 활용을 통한 통합 테스트
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
@AutoConfigureMockMvc
public class ExercisePostRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExercisePostRepository exercisePostRepository;
	
	@Autowired
	private PostLikeRepository postLikeRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentLikeRepository commentLikeRepository;
	
	/*
	 * @RequestMapping(value="/community/post/{post_id}/addLike",method = RequestMethod.POST)
	 * @LoginIdSessionNotNull
	 * public void addLikeToExercisePost(@PathVariable("post_id") Long postId)
	 */
	
	private void checkFailureAddLikeToExercisePost(User user, ExercisePost ep,int postLikeNum) {
		assertEquals(0, userRepository.findById(user.getId()).get().getPostLikes().size());
		assertEquals(0, exercisePostRepository.findById(ep.getId()).get().getPostLikes().size());
		assertEquals(postLikeNum, postLikeRepository.findAll().size());
	}
	
	@Test
	@Transactional
	@DisplayName("좋아요 추가 -> 세션에 저장된 ID로 유저 찾을 수 없음")
	public void addLikeToExercisePostUserNonExist() throws Exception {
		//Given
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		ep.setUser(user);
		
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(post("/community/post/" + ep.getId() + "/addLike")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId()+1))

		//Then
			.andExpect(status().isNoContent());
		
		 checkFailureAddLikeToExercisePost(user,ep,0);
	}
	
	@Test
	@Transactional
	@DisplayName("좋아요 추가 -> 해당 ID의 ExercisePost 없음")
	public void addLikeToExercisePostEPNonExist() throws Exception {
		//Given
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		ep.setUser(user);
		
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(post("/community/post/" + (ep.getId()+1) + "/addLike")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId()))
		
		//Then
				.andExpect(status().isNoContent());
		
		checkFailureAddLikeToExercisePost(user,ep,0);
	}
	
	@Test
	@Transactional
	@DisplayName("좋아요 추가 -> 해당 ExercisePost에 User의 PostLike가 이미 존재")
	public void addLikeToExercisePostAlredyExist() throws Exception {
		//Given
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
		
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		ep.setUser(user);
		
		exercisePostRepository.save(ep);
		
		PostLike pl = PostLikeTest.getPostLikeInstance();
		pl.setExercisePost(ep);
		pl.setUser(user);
		
		postLikeRepository.save(pl);
		
		//When
		mockMvc.perform(post("/community/post/" + ep.getId() + "/addLike")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId()))
		
		//Then
				.andExpect(status().isConflict());
		
		assertEquals(1, postLikeRepository.findAll().size());
	}
	
	@Test
	@Transactional
	@DisplayName("좋아요 추가 -> 정상")
	public void addLikeToExercisePostTest() throws Exception {
		//Given
		User user = UserTest.getUserInstance(RoleType.USER);
		userRepository.save(user);
				
		ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		ep.setUser(user);
				
		exercisePostRepository.save(ep);
		
		//When
		mockMvc.perform(post("/community/post/" + ep.getId() + "/addLike")
				.sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId()))
		
		//Then
				.andExpect(status().isOk());
		
		assertEquals(1, userRepository.findById(user.getId()).get().getPostLikes().size());
		assertEquals(1, exercisePostRepository.findById(ep.getId()).get().getPostLikes().size());
		assertEquals(1, postLikeRepository.findAll().size());
	}
	
	/*
	 *  @RequestMapping(value="/community/post/{post_id}/removeLike",method = RequestMethod.DELETE)
	 *  @LoginIdSessionNotNull
	 *  public void removeLikeToExercisePost(@PathVariable("post_id") Long postId)
	 */
	
	 @Test
	 @Transactional
	 @DisplayName("좋아요 취소 -> 해당 ID의 PostLike 없음")
	 public void removeLikeToExercisePostNonExist() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
					
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
					
		 exercisePostRepository.save(ep);
		 
		 //When
		 mockMvc.perform(delete("/community/post/" + ep.getId() + "/removelike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID	, user.getId()))
		 
		 //Then
		 		 .andExpect(status().isNoContent());
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("좋아요 취소 -> 정상")
	 public void removeLikeToExercisePostTest() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
			
		 PostLike pl = PostLikeTest.getPostLikeInstance();
		 pl.setExercisePost(ep);
		 pl.setUser(user);
			
		 postLikeRepository.save(pl);
		 
		 //When
		 mockMvc.perform(delete("/community/post/" + ep.getId() + "/removelike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID	, user.getId()))
		 
		 //Then
		 		.andExpect(status().isOk());
		 
		 assertEquals(0, userRepository.findById(user.getId()).get().getPostLikes().size());
		 assertEquals(0, exercisePostRepository.findById(ep.getId()).get().getPostLikes().size());
		 assertEquals(0, postLikeRepository.findAll().size());
	 }
	 
	 /*
	  * @RequestMapping(value="/community/post/{post_id}/comment/addlike",method = RequestMethod.POST)
	  * @LoginIdSessionNotNull
	  * public void addLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId)
	  */
	 
	 private void checkFailureAddLikeToComment(User user, Comment comment, int commentLikeNum) {
		 assertEquals(0,userRepository.findById(user.getId()).get().getCommentLikes().size());
		 assertEquals(0,commentRepository.findById(comment.getId()).get().getCommentLikes().size());
		 assertEquals(commentLikeNum,commentLikeRepository.findAll().size());
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글에 좋아요 추가 -> 세션의 저장된 ID의 유저 없음")
	 public void addLikeToCommentUserNonExist() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 //When
		 mockMvc.perform(post("/community/post/"+ep.getId()+"/comment/addlike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID,user.getId()+1)
				 .param("commentId", String.valueOf(comment.getId())))
		 
		 //Then
		 		.andExpect(status().isNoContent());
		 
		 checkFailureAddLikeToComment(user,comment,0);
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글에 좋아요 추가 -> 해당 ID의 Comment 없을때")
	 public void addLikeToCommentNonExist() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 //When
		 mockMvc.perform(post("/community/post/"+ep.getId()+"/comment/addlike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID,user.getId())
				 .param("commentId", String.valueOf(comment.getId()+1)))
		 
		 //Then
		 		.andExpect(status().isNoContent());
		 
		 checkFailureAddLikeToComment(user,comment,0);
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글에 좋아요 추가 -> 해당 유저가 이미 댓글에 좋아요 추가했을때")
	 public void addLikeToCommentAlreadyExist() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 CommentLike cl = CommentLikeTest.getCommentLikeInstance();
		 cl.setUser(user);
		 cl.setComment(comment);
		 commentLikeRepository.save(cl);
		 
		 //When
		 mockMvc.perform(post("/community/post/"+ep.getId()+"/comment/addlike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID,user.getId())
				 .param("commentId", String.valueOf(comment.getId())))
		 
		 //Then
		 		 .andExpect(status().isConflict());
		 
		 assertEquals(1, commentLikeRepository.findAll().size());
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글에 좋아요 추가 -> 정상")
	 public void addLikeToCommentTest() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 //When
		 mockMvc.perform(post("/community/post/"+ep.getId()+"/comment/addlike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID,user.getId())
				 .param("commentId", String.valueOf(comment.getId())))
				 
		 //Then
				 .andExpect(status().isOk());
		 
		 assertEquals(1,userRepository.findById(user.getId()).get().getCommentLikes().size());
		 assertEquals(1,commentRepository.findById(comment.getId()).get().getCommentLikes().size());
		 assertEquals(1,commentLikeRepository.findAll().size());
	 }
	 
	 /*
	  * @RequestMapping(value="/community/post/{post_id}/comment/removelike",method = RequestMethod.DELETE)
	  * @LoginIdSessionNotNull
	  * public void removeLikeToComment(@PathVariable("post_id") Long postId, @RequestParam("commentId") Long commentId)
	  */
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글 좋아요 취소 -> 해당 조건 만족하는 commentLike 없을때")
	 public void removeLikeToCommentNonExist() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
		 
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 //When
		 mockMvc.perform(delete("/community/post/" + ep.getId() + "/comment/removelike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				 .param("commentId", String.valueOf(comment.getId())))
		 
		 //Then
		 		 .andExpect(status().isNoContent());
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글 좋아요 취소 -> 정상")
	 public void removeLikeToCommentTest() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 CommentLike cl = CommentLikeTest.getCommentLikeInstance();
		 cl.setUser(user);
		 cl.setComment(comment);
		 commentLikeRepository.save(cl);
		 
		 //When
		 mockMvc.perform(delete("/community/post/" + ep.getId() + "/comment/removelike")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				 .param("commentId", String.valueOf(comment.getId())))
		 
		 //Then
		 		 .andExpect(status().isOk());
		 
		 assertEquals(0,userRepository.findById(user.getId()).get().getCommentLikes().size());
		 assertEquals(0,commentRepository.findById(comment.getId()).get().getCommentLikes().size());
		 assertEquals(0,commentLikeRepository.findAll().size());
	 }
	 
	 /*
	  * @RequestMapping(value="/community/post/{post_id}/removeComment",method = RequestMethod.DELETE)
	  * @LoginIdSessionNotNull
	  * public void removeCommentToExercisePost(@PathVariable("post_id") Long postId,@RequestParam("commentId") Long commentId)
	  */
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글 삭제 -> 해당 ID의 Comment 없을 때")
	 public void removeCommentToExercisePostNonExist() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 //When
		 mockMvc.perform(delete("/community/post/" + ep.getId() + "/removeComment")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				 .param("commentId", String.valueOf(comment.getId()+1)))
		 
		 //Then
		 		 .andExpect(status().isOk());
		 
		 assertEquals(1, commentRepository.findAll().size());
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글 삭제 -> 세션에 저장된 ID와 댓글 작성자의 ID 일치 하지 않으면")
	 public void removeCommentToExercisePostNotMatch() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 //When
		 mockMvc.perform(delete("/community/post/" + ep.getId() + "/removeComment")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId()+1)
				 .param("commentId", String.valueOf(comment.getId())))
	
		 //Then
		 	.andExpect(status().isOk());
		 
		 assertEquals(1, commentRepository.findAll().size());
	 }
	 
	 @Test
	 @Transactional
	 @DisplayName("댓글 삭제 -> 정상")
	 public void removeCommentToExercisePostTest() throws Exception {
		 //Given
		 User user = UserTest.getUserInstance(RoleType.USER);
		 userRepository.save(user);
			
		 ExercisePost ep = ExercisePostTest.getExercisePostInstance();
		 ep.setUser(user);
			
		 exercisePostRepository.save(ep);
		 
		 Comment comment = CommentTest.getCommentInstance();
		 comment.setUser(user);
		 comment.setExercisePost(ep);
		 
		 commentRepository.save(comment);
		 
		 //When
		 mockMvc.perform(delete("/community/post/" + ep.getId() + "/removeComment")
				 .sessionAttr(SessionUtil.LOGIN_USER_ID, user.getId())
				 .param("commentId", String.valueOf(comment.getId())))
		 //Then
		 		 .andExpect(status().isOk());
		 
		 assertEquals(0, commentRepository.findAll().size());
	 }
}
