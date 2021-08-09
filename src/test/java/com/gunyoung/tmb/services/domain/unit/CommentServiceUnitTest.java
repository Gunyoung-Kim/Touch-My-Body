package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentServiceImpl;

/**
 * {@link CommentServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) CommentServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class CommentServiceUnitTest {
	
	@Mock
	CommentRepository commentRepository;
	
	@InjectMocks
	CommentServiceImpl commentService;
	
	private Comment comment;
	
	@BeforeEach
	void setup() {
		comment = new Comment();
	}
	
	/*
	 * public Comment findById(Long id)
	 */
	@Test
	@DisplayName("ID로 Comment 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
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
	public void findByIdTest() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
		
		//When
		Comment result = commentService.findById(commentId);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 * public Comment findWithUserAndExercisePostById(Long id) 
	 */
	
	@Test
	@DisplayName("ID로 User와 ExercisePost 페치조인 후 Comment 반환 -> 존재하지 않음")
	public void findWithUserAndExercisePostByIdNonExist() {
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
	public void findWithUserAndExercisePostById() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findWithUserAndExercisePostById(commentId)).willReturn(Optional.of(comment));
		
		//When
		Comment result = commentService.findWithUserAndExercisePostById(commentId);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 * public Comment findWithCommentLikesById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 CommentLikes 페치조인 후 Comment 반환 -> 존재하지 않음")
	public void findWithCommentLikesByIdNonExist() {
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
	public void findWithCommentLikesByIdTest() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findWithCommentLikesById(commentId)).willReturn(Optional.of(comment));
		
		//When
		Comment result = commentService.findWithCommentLikesById(commentId);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 *  public List<Comment> findAllByExercisePostId(Long postId)
	 */
	
	@Test
	@DisplayName("ExercisePost ID를 만족하는 Comment들 반환 -> 정상")
	public void findAllByExercisePostIdTest() {
		//Given
		Long postId = Long.valueOf(1);
		List<Comment> commentList = new ArrayList<>();
		given(commentRepository.findAllByExercisePostIdCustom(postId)).willReturn(commentList);
		
		//When
		List<Comment> result = commentService.findAllByExercisePostId(postId);
		
		//Then
		assertEquals(commentList, result);
	}
	
	/*
	 * public Page<Comment> findAllByUserIdOrderByCreatedAtASC(Long userId,Integer pageNum, int page_size)
	 */
	
	@Test
	@DisplayName("User ID를 만족하는 Comment들 생성 오래된순으로 페이지 반환 -> 정상")
	public void findAllByUserIdOrderByCreatedAtASCTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
		
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		Page<Comment> commentPage = new PageImpl<>(new ArrayList<>());
		
		given(commentRepository.findAllByUserIdOrderByCreatedAtASCCustom(userId, pageRequest)).willReturn(commentPage);
		
		//When
		Page<Comment> result =  commentService.findAllByUserIdOrderByCreatedAtASC(userId, pageNum, pageSize);
		
		//Then
		assertEquals(commentPage, result);
	}
	
	/*
	 * public Page<Comment> findAllByUserIdOrderByCreatedAtDESC(Long userId,Integer pageNum, int page_size)
	 */
	
	@Test
	@DisplayName("User ID를 만족하는 Comment들 생성 최신순으로 페이지 반환 -> 정상")
	public void findAllByUserIdOrderByCreatedAtDESCTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
				
		PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
		Page<Comment> commentPage = new PageImpl<>(new ArrayList<>());
				
		given(commentRepository.findAllByUserIdOrderByCreatedAtDescCustom(userId, pageRequest)).willReturn(commentPage);
				
		//When
		Page<Comment> result =  commentService.findAllByUserIdOrderByCreatedAtDESC(userId, pageNum, pageSize);
				
		//Then
		assertEquals(commentPage, result);
	}
	
	/*
	 * public Comment save(Comment comment)
	 */
	
	@Test
	@DisplayName("Comment 저장")
	public void saveTest() {
		//Given
		given(commentRepository.save(comment)).willReturn(comment);
		
		//When
		Comment result = commentService.save(comment);
		
		//Then
		assertEquals(comment, result);
	}
	
	/*
	 * public Comment saveWithUserAndExercisePost(Comment comment, User user, ExercisePost exercisePost) 
	 */
	
	@Test
	@DisplayName("User, ExercisePost 와 연관관계 생성 후 저장 -> 연관 관계 확인")
	public void saveWithUserAndExercisePostTest() {
		//Given
		User user = new User();
		ExercisePost exercisePost = new ExercisePost();
		
		//When
		commentService.saveWithUserAndExercisePost(comment, user, exercisePost);
		
		//Then
		assertEquals(user, comment.getUser());
		assertEquals(exercisePost, comment.getExercisePost());
	}
	
	@Test
	@DisplayName("User, ExercisePost 와 연관관계 생성 후 저장 -> 저장 확인")
	public void saveWithUserAndExercisePostSaveTest() {
		//Given
		User user = new User();
		ExercisePost exercisePost = new ExercisePost();
				
		//When
		commentService.saveWithUserAndExercisePost(comment, user, exercisePost);
				
		//Then
		
		then(commentRepository).should(times(1)).save(comment);
	}
	
	/*
	 * public void delete(Comment comment)
	 */
	
	@Test
	@DisplayName("Comment 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		commentService.delete(comment);
		
		//Then
		then(commentRepository).should(times(1)).delete(comment);
	}
	
	/*
	 * public void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("Comment ID로 찾고 삭제 -> Comment 없음")
	public void deleteByIdNonExist() {
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
	public void deleteByIdTest() {
		//Given
		Long commentId = Long.valueOf(1);
		given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
		
		//When
		commentService.deleteById(commentId);
		
		//Then
		then(commentRepository).should(times(1)).delete(comment);
	}
	
	/*
	 * public void checkIsMineAndDelete(Long userId, Long commentId)
	 */
	
	@Test
	@DisplayName("해당 Comment ID의 Comment 작성자의 ID가 userID와 일치하면 삭제 -> 일치하지 않음")
	public void checkIsMineAndDeleteNotMatch() {
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
	public void checkIsMineAndDeleteTest() {
		//Given
		Long userId = Long.valueOf(1), commentId = Long.valueOf(1);
		given(commentRepository.findByUserIdAndCommentId(userId, commentId)).willReturn(Optional.of(comment));
		
		//When
		commentService.checkIsMineAndDelete(userId, commentId);
		
		//Then
		then(commentRepository).should(times(1)).delete(comment);
	}
	
	/*
	 * public long countByUserId(Long userId)
	 */
	
	@Test
	@DisplayName("User ID 만족하는 Comment들 개수 반환 -> 정상")
	public void countByUserIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		//When
		commentService.countByUserId(userId);
		
		//Then
		then(commentRepository).should(times(1)).countByUserId(userId);
	}
	
	/*
	 * public List<CommentForPostViewDTO> getCommentForPostViewDTOsByExercisePostId(Long postId)
	 */
	
	@Test
	@DisplayName("해당 exercisePost id 를 만족하는 Comment 객체들을 CommentForPostViewDTO로 변환해서 반환 -> 정상")
	public void getCommentForPostViewDTOsByExercisePostIdTest() {
		//Given
		Long exerciePostId = Long.valueOf(1);
		
		//When
		commentService.getCommentForPostViewDTOsByExercisePostId(exerciePostId);
		
		//Then
		then(commentRepository).should(times(1)).findForCommentForPostViewDTOByExercisePostId(exerciePostId);
	}
}
