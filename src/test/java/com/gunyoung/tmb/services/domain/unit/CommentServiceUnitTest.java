package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.precondition.PreconditionViolationException;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentServiceImpl;
import com.gunyoung.tmb.services.domain.like.CommentLikeService;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.UserTest;

/**
 * {@link CommentServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) CommentServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest {
	
	@Mock
	CommentRepository commentRepository;
	
	@Mock
	CommentLikeService commentLikeService;
	
	@InjectMocks
	CommentServiceImpl commentService;
	
	private Comment comment;
	
	@BeforeEach
	void setup() {
		comment = Comment.builder()
				.id(Long.valueOf(24))
				.build();
	}
	
	/*
	 * Comment findById(Long id)
	 */
	@Test
	@DisplayName("ID로 Comment 찾기 -> 존재하지 않음")
	void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(commentRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Comment result = commentService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Comment 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
		
		//When
		Comment result = commentService.findById(commentId);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 * Comment findWithUserAndExercisePostById(Long id) 
	 */
	
	@Test
	@DisplayName("ID로 User와 ExercisePost 페치조인 후 Comment 반환 -> 존재하지 않음")
	void findWithUserAndExercisePostByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(commentRepository.findWithUserAndExercisePostById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Comment result = commentService.findWithUserAndExercisePostById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 User와 ExercisePost 페치조인 후 Comment 반환 -> 정상")
	void findWithUserAndExercisePostById() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findWithUserAndExercisePostById(commentId)).willReturn(Optional.of(comment));
		
		//When
		Comment result = commentService.findWithUserAndExercisePostById(commentId);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 * Comment findWithCommentLikesById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 CommentLikes 페치조인 후 Comment 반환 -> 존재하지 않음")
	void findWithCommentLikesByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(commentRepository.findWithCommentLikesById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Comment result = commentService.findWithCommentLikesById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 CommentLikes 페치조인 후 Comment 반환 -> 정상")
	void findWithCommentLikesByIdTest() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findWithCommentLikesById(commentId)).willReturn(Optional.of(comment));
		
		//When
		Comment result = commentService.findWithCommentLikesById(commentId);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 *  List<Comment> findAllByExercisePostId(Long postId)
	 */
	
	@Test
	@DisplayName("ExercisePost ID를 만족하는 Comment들 반환 -> 정상")
	void findAllByExercisePostIdTest() {
		//Given
		Long postId = Long.valueOf(1);
		List<Comment> commentList = new ArrayList<>();
		given(commentRepository.findAllByExercisePostIdInQuery(postId)).willReturn(commentList);
		
		//When
		List<Comment> result = commentService.findAllByExercisePostId(postId);
		
		//Then
		assertEquals(commentList, result);
	}
	
	/*
	 * Page<Comment> findAllByUserIdOrderByCreatedAtASC(Long userId,Integer pageNum, int page_size)
	 */
	
	@Test
	@DisplayName("User ID를 만족하는 Comment들 생성 오래된순으로 페이지 반환 -> 정상")
	void findAllByUserIdOrderByCreatedAtASCTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
		
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		Page<Comment> commentPage = new PageImpl<>(new ArrayList<>());
		
		given(commentRepository.findAllByUserIdOrderByCreatedAtAscInPage(userId, pageRequest)).willReturn(commentPage);
		
		//When
		Page<Comment> result =  commentService.findAllByUserIdOrderByCreatedAtAsc(userId, pageNum, pageSize);
		
		//Then
		assertEquals(commentPage, result);
	}
	
	/*
	 * Page<Comment> findAllByUserIdOrderByCreatedAtDESC(Long userId,Integer pageNum, int page_size)
	 */
	
	@Test
	@DisplayName("User ID를 만족하는 Comment들 생성 최신순으로 페이지 반환 -> 정상")
	void findAllByUserIdOrderByCreatedAtDESCTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
				
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		Page<Comment> commentPage = new PageImpl<>(new ArrayList<>());
				
		given(commentRepository.findAllByUserIdOrderByCreatedAtDescInPage(userId, pageRequest)).willReturn(commentPage);
				
		//When
		Page<Comment> result =  commentService.findAllByUserIdOrderByCreatedAtDesc(userId, pageNum, pageSize);
				
		//Then
		assertEquals(commentPage, result);
	}
	
	/*
	 * Comment save(Comment comment)
	 */
	
	@Test
	@DisplayName("Comment 저장")
	void saveTest() {
		//Given
		given(commentRepository.save(comment)).willReturn(comment);
		
		//When
		Comment result = commentService.save(comment);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 * Comment saveWithUserAndExercisePost(Comment comment, User user, ExercisePost exercisePost) 
	 */
	
	@Test
	@DisplayName("User, ExercisePost 와 연관관계 생성 후 저장 -> 입력 comment null")
	void saveWithUserAndExercisePostTestNullComment() {
		//Given
		User user = UserTest.getUserInstance();
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		
		//When, Then 
		assertThrows(PreconditionViolationException.class, () -> {
			commentService.saveWithUserAndExercisePost(null, user, exercisePost);
		});
	}
	
	@Test
	@DisplayName("User, ExercisePost 와 연관관계 생성 후 저장 -> 입력 user null")
	void saveWithUserAndExercisePostTestNullUser() {
		//Given
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		
		//When, Then 
		assertThrows(PreconditionViolationException.class, () -> {
			commentService.saveWithUserAndExercisePost(comment, null, exercisePost);
		});
	}
	
	@Test
	@DisplayName("User, ExercisePost 와 연관관계 생성 후 저장 -> 입력 exercisePost null")
	void saveWithUserAndExercisePostTestNullExercisePost() {
		//Given
		User user = UserTest.getUserInstance();
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			commentService.saveWithUserAndExercisePost(comment, user, null);
		});
	}
	
	@Test
	@DisplayName("User, ExercisePost 와 연관관계 생성 후 저장 -> 연관 관계 확인")
	void saveWithUserAndExercisePostTest() {
		//Given
		User user = UserTest.getUserInstance();
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		
		//When
		commentService.saveWithUserAndExercisePost(comment, user, exercisePost);
		
		//Then
		assertEquals(user, comment.getUser());
		assertEquals(exercisePost, comment.getExercisePost());
	}
	
	@Test
	@DisplayName("User, ExercisePost 와 연관관계 생성 후 저장 -> 저장 확인")
	void saveWithUserAndExercisePostSaveTest() {
		//Given
		User user = UserTest.getUserInstance();
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
				
		//When
		commentService.saveWithUserAndExercisePost(comment, user, exercisePost);
				
		//Then
		
		then(commentRepository).should(times(1)).save(comment);
	}
	
	/*
	 * void delete(Comment comment)
	 */
	
	@Test
	@DisplayName("Comment 삭제 -> null 값 들어옴")
	void deleteTestNullComment() {
		//Given
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			commentService.delete(null);
		});
	}
	
	@Test
	@DisplayName("Comment 삭제 -> 정상, check CommentRepository")
	void deleteTestCheckCommentRepo() {
		//Given
		
		//When
		commentService.delete(comment);
		
		//Then
		then(commentRepository).should(times(1)).deleteByIdInQuery(comment.getId());
	}
	
	@Test
	@DisplayName("Comment 삭제 -> 정상, check CommentLikeService")
	void deleteTestCheckCommentLikeService() {
		//Given
		Long commentId = Long.valueOf(24);
		comment.setId(commentId);
		
		//When
		commentService.delete(comment);
		
		//Then
		then(commentLikeService).should(times(1)).deleteAllByCommentId(commentId);
	}
	
	/*
	 * void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("Comment ID로 찾고 삭제 -> Comment 없음")
	void deleteByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(commentRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		commentService.deleteById(nonExistId);
		
		//Then
		then(commentRepository).should(never()).delete(comment);
	}
	
	@Test
	@DisplayName("Comment ID로 찾고 삭제 -> 정상")
	void deleteByIdTest() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
		
		//When
		commentService.deleteById(commentId);
		
		//Then
		then(commentRepository).should(times(1)).deleteByIdInQuery(comment.getId());
	}
	
	/*
	 * void checkIsMineAndDelete(Long userId, Long commentId)
	 */
	
	@Test
	@DisplayName("해당 Comment ID의 Comment 작성자의 ID가 userID와 일치하면 삭제 -> 일치하지 않음")
	void checkIsMineAndDeleteNotMatch() {
		//Given
		Long userId = Long.valueOf(1), commentId = Long.valueOf(1);
		given(commentRepository.findByUserIdAndCommentId(userId, commentId)).willReturn(Optional.empty());
		
		//When
		commentService.checkIsMineAndDelete(userId, commentId);
		
		//Then
		then(commentRepository).should(never()).delete(any(Comment.class));
	}
	
	@Test
	@DisplayName("해당 Comment ID의 Comment 작성자의 ID가 userID와 일치하면 삭제 -> 정상")
	void checkIsMineAndDeleteTest() {
		//Given
		Long userId = Long.valueOf(1), commentId = Long.valueOf(1);
		given(commentRepository.findByUserIdAndCommentId(userId, commentId)).willReturn(Optional.of(comment));
		
		//When
		commentService.checkIsMineAndDelete(userId, commentId);
		
		//Then
		then(commentRepository).should(times(1)).deleteByIdInQuery(comment.getId());
	}
	
	/*
	 * void deleteAllByUserId(Long userId)
	 */
	
	@Test
	@DisplayName("User ID에 해당하는 Comment 일괄 삭제 -> 정상, No OneToMany Delete,check CommentRepository") 
	void deleteAllByUserIdTestNoOneToManyCheckCommentRepo() {
		//Given
		Long userId = Long.valueOf(52);
		List<Comment> comments = new ArrayList<>();
		given(commentRepository.findAllByUserIdInQuery(userId)).willReturn(comments);
		
		//When
		commentService.deleteAllByUserId(userId);
		
		//Then
		then(commentRepository).should(times(1)).deleteAllByUserIdInQuery(userId);
	}
	
	@Test
	@DisplayName("User ID에 해당하는 Comment 일괄 삭제 -> 정상, check CommentLikeService")
	void deleteAllByUserIdTestCheckCommentLikeService() {
		//Given
		List<Comment> comments = new ArrayList<>();
		Long commentsId = Long.valueOf(65);
		int givenCommentNum = 13;
		for(int i=0;i<givenCommentNum;i++) {
			Comment comment = CommentTest.getCommentInstance();
			comment.setId(commentsId);
			comments.add(comment);
		}
		
		Long userId = Long.valueOf(52);
		given(commentRepository.findAllByUserIdInQuery(userId)).willReturn(comments);
		
		//When
		commentService.deleteAllByUserId(userId);
		
		//Then
		then(commentLikeService).should(times(givenCommentNum)).deleteAllByCommentId(commentsId);
	}
	
	/*
	 * void deleteAllByExercisePostId(Long exercisePostId)
	 */
	
	@Test
	@DisplayName("ExercisePost ID에 해당하는 Comment 일괄 삭제 -> 정상, No OneToMany Delete ,check CommentRepository") 
	void deleteAllByExercisePostIdTestNoOneToManyCheckCommentRepo() {
		//Given
		Long exercisePostId = Long.valueOf(52);
		List<Comment> comments = new ArrayList<>();
		given(commentRepository.findAllByExercisePostIdInQuery(exercisePostId)).willReturn(comments);
		
		//When
		commentService.deleteAllByExercisePostId(exercisePostId);
		
		//Then
		then(commentRepository).should(times(1)).deleteAllByExercisePostIdInQuery(exercisePostId);
	}
	
	@Test
	@DisplayName("ExercisePost ID에 해당하는 Comment 일괄 삭제 -> 정상, check CommentLikeService")
	void deleteAllByExercisePostIdTestCheckCommentLikeService() {
		//Given
		List<Comment> comments = new ArrayList<>();
		Long commentsId = Long.valueOf(24);
		int givenCommentNum = 8;
		for(int i=0;i<givenCommentNum;i++) {
			Comment comment = CommentTest.getCommentInstance();
			comment.setId(commentsId);
			comments.add(comment);
		}
		
		Long exercisePostId = Long.valueOf(52);
		given(commentRepository.findAllByExercisePostIdInQuery(exercisePostId)).willReturn(comments);
		
		//When
		commentService.deleteAllByExercisePostId(exercisePostId);
		
		//Then
		then(commentLikeService).should(times(givenCommentNum)).deleteAllByCommentId(commentsId);
	}
	
	/*
	 * long countByUserId(Long userId)
	 */
	
	@Test
	@DisplayName("User ID 만족하는 Comment들 개수 반환 -> 정상")
	void countByUserIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		//When
		commentService.countByUserId(userId);
		
		//Then
		then(commentRepository).should(times(1)).countByUserId(userId);
	}
	
	/*
	 * List<CommentForPostViewDTO> getCommentForPostViewDTOsByExercisePostId(Long postId)
	 */
	
	@Test
	@DisplayName("해당 exercisePost id 를 만족하는 Comment 객체들을 CommentForPostViewDTO로 변환해서 반환 -> 정상")
	void getCommentForPostViewDTOsByExercisePostIdTest() {
		//Given
		Long exerciePostId = Long.valueOf(1);
		
		//When
		commentService.getCommentForPostViewDTOsByExercisePostId(exerciePostId);
		
		//Then
		then(commentRepository).should(times(1)).findForCommentForPostViewDTOByExercisePostId(exerciePostId);
	}
}
