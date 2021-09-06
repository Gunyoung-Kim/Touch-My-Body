package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.util.CommentTest;
import com.gunyoung.tmb.util.ExercisePostTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.util.tag.Integration;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * CommentService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class CommentServiceTest {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	private Comment comment;
	
	@BeforeEach
	void setup() {
		comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
	}
	
	@AfterEach
	void tearDown() {
		commentRepository.deleteAll();
	}
	
	/*
	 *  public Comment findById(Long id)
	 */
	@Test
	@DisplayName("id로 Comment 찾기 -> 해당 id의 comment 없음")
	public void findByIdNonExist() {
		//Given
		Long nonExistCommentId = CommentTest.getNonExistCommentId(commentRepository);
		
		//When
		Comment result = commentService.findById(nonExistCommentId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("id로 Comment 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existCommentId = comment.getId();
		
		//When
		Comment result = commentService.findById(existCommentId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  public List<Comment> findByExercisePostId(Long postId)
	 */
	@Test
	@Transactional
	@DisplayName("ExercisePost id로 Comment들 찾기 ->정상")
	public void findByExercisePostIdTest() {
		//Given
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		Long exercisePostId = exercisePost.getId();
		
		List<Comment> comments = commentRepository.findAll();
		int givenCommentNum = comments.size();
		for(Comment c: comments) {
			c.setExercisePost(exercisePost);
		}
		
		commentRepository.saveAll(comments);
		
		//When
		List<Comment> result = commentService.findAllByExercisePostId(exercisePostId);
		
		//Then
		assertEquals(givenCommentNum, result.size());
	}
	
	/*
	 * public List<Comment> findAllByUserIdOrderByCreatedAtASC(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 오래된순서대로 ->정상, 개수 확인")
	public void findAllByUserIdOrderByCreatedAtASCTestCheckCount() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		List<Comment> comments = commentRepository.findAll();
		int givenCommentNum = comments.size();
		for(Comment c: comments) {
			c.setUser(user);
		}
		commentRepository.saveAll(comments);
		
		//When
		
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtAsc(user.getId(),1,PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertEquals(Math.min(givenCommentNum, PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE), result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 오래된순서대로 ->정상, 정렬 순서 확인")
	public void findAllByUserIdOrderByCreatedAtASCTestCheckSorting() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		List<Comment> newComments = new ArrayList<>();
		for(int i=0;i<2;i++) {
			Comment newComment = CommentTest.getCommentInstance();
			newComments.add(newComment);
		}
		commentRepository.saveAll(newComments);
		
		
		List<Comment> comments = commentRepository.findAll();
		for(Comment c: comments) {
			c.setUser(user);
		}
		commentRepository.saveAll(comments);
		
		//When
		
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtAsc(user.getId(),1,PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertTrue(result.get(0).getCreatedAt().isBefore(result.get(1).getCreatedAt()));
	}
	
	/*
	 *  public List<Comment> findAllByUserIdOrderByCreatedAtDESC(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 최신순서대로 ->정상 , 개수 확인")
	public void findAllByUserIdOrderByCreatedAtDESCTestCheckCount() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		List<Comment> comments = commentRepository.findAll();
		long givenCommentNum = comments.size();
		for(Comment c: comments) {
			c.setUser(user);
		}
		commentRepository.saveAll(comments);
		
		//When
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtDesc(user.getId(),1,PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertEquals(Math.min(givenCommentNum, PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE), result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 최신순서대로 ->정상")
	public void findAllByUserIdOrderByCreatedAtDESCTest() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		List<Comment> newComments = new ArrayList<>();
		for(int i=0;i<2;i++) {
			Comment newComment = CommentTest.getCommentInstance();
			newComments.add(newComment);
		}
		commentRepository.saveAll(newComments);
		
		
		List<Comment> comments = commentRepository.findAll();
		for(Comment c: comments) {
			c.setUser(user);
		}
		
		commentRepository.saveAll(comments);
		
		//When
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtDesc(user.getId(),1,PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertTrue(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()));
	}
	
	/*
	 *  public Comment save(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("Comment 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		Long commentId = comment.getId();
		comment.setContents("changed Contents");
		
		//When
		commentService.save(comment);
		
		//Then
		Comment result = commentRepository.findById(commentId).get();
		assertEquals(result.getContents(),"changed Contents");
	}
	
	@Test
	@Transactional
	@DisplayName("Comment 추가하기 -> 정상")
	public void saveTest() {
		//Given
		Comment newComment = CommentTest.getCommentInstance();
		Long givenCommentNum = commentRepository.count();
		
		//When
		commentService.save(newComment);
		
		//Then
		assertEquals(givenCommentNum+1, commentRepository.count());
	}
	
	/*
	 * public Comment saveWithUserAndExercisePost(User user, ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("User, ExercisePost의 Comment 추가 -> 정상")
	public void saveWithUserAndExercisePostTest() {
		//Given
		Comment comment = CommentTest.getCommentInstance();
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		
		long givenCommentNum = commentRepository.count();
		
		//When
		commentService.saveWithUserAndExercisePost(comment,user, exercisePost);
		
		//Then
		assertEquals(givenCommentNum + 1 , commentRepository.count());
	}
	
	/*
	 *  public void delete(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("Comment 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		Long givenCommentNum = commentRepository.count();
		
		//When
		commentService.delete(comment);
		
		//Then
		assertEquals(givenCommentNum-1,commentRepository.count());
	}
	
	/*
	 * public void checkIsMineAndDelete(Long userId, Long commentId) {
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID, Comment ID로 User의 Comment인지 확인 후 맞다면 삭제 -> User의 Comment 아님")
	public void checkIsMineAndDeleteNotMine() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		comment.setUser(user);
		commentRepository.save(comment);
		
		Long otherUserId = user.getId() + 1;
		Long commentId = comment.getId();
	
		//When
		commentService.checkIsMineAndDelete(otherUserId, commentId);
		
		//Then
		assertTrue(commentRepository.existsById(commentId));
	}
	
	@Test
	@Transactional
	@DisplayName("User ID, Comment ID로 User의 Comment인지 확인 후 맞다면 삭제 -> 정상")
	public void checkIsMineAndDeleteTest() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
	
		comment.setUser(user);
		commentRepository.save(comment);
		
		Long userId = user.getId();
		Long commentId = comment.getId();
		
		//When
		commentService.checkIsMineAndDelete(userId, commentId);
		
		//Then
		assertFalse(commentRepository.existsById(commentId));
	}
	
	/*
	 * public long countByUserId(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID를 만족하는 Comment 들 개수 가져오기 -> 정상")
	public void countByUserIdTest() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		List<Comment> comments = commentRepository.findAll();
		long givenCommentNum = comments.size();
		for(Comment c: comments) {
			c.setUser(user);
		}
		commentRepository.saveAll(comments);
		
		Long userId = user.getId();
		
		//When
		
		long result = commentService.countByUserId(userId);
		
		//Then
		
		assertEquals(givenCommentNum, result);
	}
	
	/*
	 *  public List<CommentForPostViewDTO> getCommentForCommentForPostViewDTOsByExercisePostId(Long postId)
	 */
	
	@Test
	@Transactional
	@DisplayName("해당 exercisePost id 를 만족하는 Comment 객체들을 CommentForPostViewDTO로 변환해서 반환 -> 정상")
	public void getCommentForCommentForPostViewDTOsByExercisePostIdTest() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		Long exercisePostId = exercisePost.getId();
		
		List<Comment> comments = commentRepository.findAll();
		long givenCommentNum = comments.size();
		for(Comment c: comments) {
			c.setExercisePost(exercisePost);
			c.setUser(user);
		}
		commentRepository.saveAll(comments);
		
		//When
		List<CommentForPostViewDTO> result = commentService.getCommentForPostViewDTOsByExercisePostId(exercisePostId);
		
		//Then
		assertEquals(givenCommentNum, result.size());
	}
}
