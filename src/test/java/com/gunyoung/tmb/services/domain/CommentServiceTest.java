package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.error.exceptions.nonexist.CommentNotFoundedException;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.testutil.CommentLikeTest;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * CommentService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class CommentServiceTest {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
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
	 *  Comment findById(Long id)
	 */
	@Test
	@DisplayName("id로 Comment 찾기 -> 해당 id의 comment 없음")
	void findByIdNonExist() {
		//Given
		Long nonExistCommentId = CommentTest.getNonExistCommentId(commentRepository);
		
		//When, Then 
		assertThrows(CommentNotFoundedException.class, () -> {
			commentService.findById(nonExistCommentId);
		});
	}
	
	@Test
	@DisplayName("id로 Comment 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long existCommentId = comment.getId();
		
		//When
		Comment result = commentService.findById(existCommentId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  List<Comment> findByExercisePostId(Long postId)
	 */
	@Test
	@Transactional
	@DisplayName("ExercisePost id로 Comment들 찾기 ->정상")
	void findByExercisePostIdTest() {
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
	 * List<Comment> findAllByUserIdOrderByCreatedAtASC(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 오래된순서대로 ->정상, 개수 확인")
	void findAllByUserIdOrderByCreatedAtASCTestCheckCount() {
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
		
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtAsc(user.getId(),1,PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertEquals(Math.min(givenCommentNum, PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()), result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 오래된순서대로 ->정상, 정렬 순서 확인")
	void findAllByUserIdOrderByCreatedAtASCTestCheckSorting() {
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
		
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtAsc(user.getId(),1,PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertTrue(result.get(0).getCreatedAt().isBefore(result.get(1).getCreatedAt()));
	}
	
	/*
	 *  List<Comment> findAllByUserIdOrderByCreatedAtDESC(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 최신순서대로 ->정상 , 개수 확인")
	void findAllByUserIdOrderByCreatedAtDESCTestCheckCount() {
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
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtDesc(user.getId(),1,PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertEquals(Math.min(givenCommentNum, PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()), result.size());
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 최신순서대로 ->정상")
	void findAllByUserIdOrderByCreatedAtDESCTest() {
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
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtDesc(user.getId(),1,PageSize.COMMENT_FOR_MANAGE_PAGE_SIZE.getSize()).getContent();
		
		//Then
		assertTrue(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()));
	}
	
	/*
	 *  Comment save(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("Comment 수정하기 -> 정상")
	void mergeTest() {
		//Given
		Long commentId = comment.getId();
		comment.setContents("changed Contents");
		
		//When
		commentService.save(comment);
		
		//Then
		Comment result = commentRepository.findById(commentId).get();
		assertEquals("changed Contents", result.getContents());
	}
	
	@Test
	@Transactional
	@DisplayName("Comment 추가하기 -> 정상")
	void saveTest() {
		//Given
		Comment newComment = CommentTest.getCommentInstance();
		Long givenCommentNum = commentRepository.count();
		
		//When
		commentService.save(newComment);
		
		//Then
		assertEquals(givenCommentNum + 1, commentRepository.count());
	}
	
	/*
	 * Comment saveWithUserAndExercisePost(User user, ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("User, ExercisePost의 Comment 추가 -> 정상")
	void saveWithUserAndExercisePostTest() {
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
	 *  void delete(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("Comment 삭제하기 -> 정상, Check Comment Delete")
	void deleteTest() {
		//Given
		Long givenCommentNum = commentRepository.count();
		
		//When
		commentService.delete(comment);
		
		//Then
		assertEquals(givenCommentNum-1,commentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Comment 삭제하기 -> 정상, Check CommentLike Delete")
	void deleteTestCheckCommentLikeDelete() {
		//Given
		CommentLike commentLike = CommentLikeTest.getCommentLikeInstance();
		commentLike.setComment(comment);
		commentLikeRepository.save(commentLike);
		
		Long commentLikeId = commentLike.getId();
		
		//When
		commentService.delete(comment);
		
		//Then
		assertFalse(commentLikeRepository.existsById(commentLikeId));
	}
	
	/*
	 * void checkIsMineAndDelete(Long userId, Long commentId) {
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID, Comment ID로 User의 Comment인지 확인 후 맞다면 삭제 -> User의 Comment 아님")
	void checkIsMineAndDeleteNotMine() {
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
	void checkIsMineAndDeleteTest() {
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
	 * void deleteAllByUserId(Long userId)
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 일괄 삭제 -> 정상, Check Comments Delete")
	void deleteAllByUserIdTestCheckCommentDelete() {
		//Given
		int addCommentNum = 7;
		addComments(addCommentNum);
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		saveAllCommentWithUser(user);
		
		Long userId = user.getId();
		
		//When
		commentService.deleteAllByUserId(userId);
		
		//Then
		assertEquals(0, commentRepository.count());
	}
	
	private void saveAllCommentWithUser(User user) {
		List<Comment> comments = commentRepository.findAll();
		for(Comment comment: comments) {
			comment.setUser(user);
		}
		commentRepository.saveAll(comments);
	}
	
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 일괄 삭제 -> 정상, Check CommentLike Delete")
	void deleteAllByUserIdTestCheckCommentLikeDelete() {
		//Given
		int addCommentNum = 5;
		addComments(addCommentNum);
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		saveAllCommentWithUserAndAddOneCommentLikeForEach(user);
		
		Long userId = user.getId();
		
		//When
		commentService.deleteAllByUserId(userId);
		
		//Then
		assertEquals(0, commentLikeRepository.count());
	}
	
	private void saveAllCommentWithUserAndAddOneCommentLikeForEach(User user) {
		List<Comment> comments = commentRepository.findAll();
		List<CommentLike> newCommentLikes = new ArrayList<>();
		for(Comment comment: comments) {
			comment.setUser(user);
			
			CommentLike commentLike = CommentLikeTest.getCommentLikeInstance();
			commentLike.setComment(comment);
			newCommentLikes.add(commentLike);
		}
		commentRepository.saveAll(comments);
		commentLikeRepository.saveAll(newCommentLikes);
	}
	
	/*
	 * void deleteAllByExercisePostId(Long exercisePostId)
	 */
	
	@Test
	@Transactional
	@DisplayName("ExercisePost ID로 Comment 일괄 삭제 -> 정상 , Check Comments Delete")
	void deleteAllByExercisePostIdTestCheckCommentsDelete() {
		//Given
		int addCommentNum = 6;
		addComments(addCommentNum);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		saveAllCommentsWithExercisePost(exercisePost);
		
		Long exercisePostId = exercisePost.getId();
		
		//When
		commentService.deleteAllByExercisePostId(exercisePostId);
		
		//Then
		assertEquals(0, commentRepository.count());
	}
	
	private void saveAllCommentsWithExercisePost(ExercisePost exercisePost) {
		List<Comment> comments = commentRepository.findAll();
		for(Comment comment: comments) {
			comment.setExercisePost(exercisePost);
		}
		commentRepository.saveAll(comments);
	}
	
	@Test
	@Transactional
	@DisplayName("ExercisePost ID로 Comment 일괄 삭제 -> 정상 , Check CommentLike Delete")
	void deleteAllByExercisePostIdTestCheckCommentLikeDelete() {
		//Given
		int addCommentNum = 6;
		addComments(addCommentNum);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		saveAllCommentsWithExercisePostAndAddCommentLikeForEach(exercisePost);
		
		Long exercisePostId = exercisePost.getId();
		
		//When
		commentService.deleteAllByExercisePostId(exercisePostId);
		
		//Then
		assertEquals(0, commentLikeRepository.count());
	}
	
	private void saveAllCommentsWithExercisePostAndAddCommentLikeForEach(ExercisePost exercisePost) {
		List<Comment> comments = commentRepository.findAll();
		List<CommentLike> newCommentLikes = new ArrayList<>();
		for(Comment comment: comments) {
			CommentLike commentLike = CommentLikeTest.getCommentLikeInstance();
			commentLike.setComment(comment);
			newCommentLikes.add(commentLike);
			
			comment.setExercisePost(exercisePost);
		}
		
		commentRepository.saveAll(comments);
		commentLikeRepository.saveAll(newCommentLikes);
	}
	
	private void addComments(int addCommentNum) {
		List<Comment> comments = new ArrayList<>();
		for(int i=0; i < addCommentNum; i++) {
			Comment comment = CommentTest.getCommentInstance();
			comments.add(comment);
		}
		commentRepository.saveAll(comments);
	}
	
	/*
	 * long countByUserId(Long userId)
	 */
	
	@Test
	@Transactional
	@DisplayName("User ID를 만족하는 Comment 들 개수 가져오기 -> 정상")
	void countByUserIdTest() {
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
	 *  List<CommentForPostViewDTO> getCommentForCommentForPostViewDTOsByExercisePostId(Long postId)
	 */
	
	@Test
	@Transactional
	@DisplayName("해당 exercisePost id 를 만족하는 Comment 객체들을 CommentForPostViewDTO로 변환해서 반환 -> 정상")
	void getCommentForCommentForPostViewDTOsByExercisePostIdTest() {
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
