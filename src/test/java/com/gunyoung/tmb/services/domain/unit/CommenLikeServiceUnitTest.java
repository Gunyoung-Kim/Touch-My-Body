package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.services.domain.like.CommentLikeServiceImpl;

/**
 * {@link CommenLikeServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) CommenLikeServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class CommenLikeServiceUnitTest {
	
	@Mock
	CommentLikeRepository commentLikeRepository;
	
	@InjectMocks
	CommentLikeServiceImpl commentLikeService;
	
	private CommentLike commentLike;
	
	@BeforeEach
	void setup() {
		commentLike = new CommentLike();
	}
	
	/*
	 * public CommentLike findById(Long id)
	 */
	@Test
	@DisplayName("ID로 CommentLike찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(commentLikeRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		CommentLike result = commentLikeService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 CommentLike 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existId = Long.valueOf(1);
		given(commentLikeRepository.findById(existId)).willReturn(Optional.of(commentLike));
		
		//When
		CommentLike result = commentLikeService.findById(existId);
		
		//Then
		assertEquals(commentLike, result);
	}
	
	/*
	 * public CommentLike findByUserIdAndCommentId(Long userId, Long commentId)
	 */
	
	@Test
	@DisplayName("User Id, Comment Id 로 CommentLike 찾기 -> 존재하지 않음")
	public void findByUserIdAndCommentIdNonExist() {
		//Given
		Long userId = Long.valueOf(1);
		Long commentId = Long.valueOf(1);
		given(commentLikeRepository.findByUserIdAndCommentId(userId, commentId)).willReturn(Optional.empty());
		
		//When
		CommentLike result = commentLikeService.findByUserIdAndCommentId(userId, commentId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("User Id, Comment Id 로 CommentLike 찾기 -> 정상")
	public void findByUserIdAndCommentIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		Long commentId = Long.valueOf(1);
		given(commentLikeRepository.findByUserIdAndCommentId(userId, commentId)).willReturn(Optional.of(commentLike));
		
		//When
		CommentLike result = commentLikeService.findByUserIdAndCommentId(userId, commentId);
		
		//Then
		assertEquals(commentLike, result);
	}
	
	/*
	 * public CommentLike save(CommentLike commentLike) 
	 */
	
	@Test
	@DisplayName("Comment 저장 -> 정상")
	public void saveTest() {
		//Given
		given(commentLikeRepository.save(commentLike)).willReturn(commentLike);
		
		//When
		CommentLike result = commentLikeService.save(commentLike);
		
		//Then
		assertEquals(commentLike, result);
	}

	/*
	 * public void delete(CommentLike commentLike)
	 */
	
	@Test
	@DisplayName("CommentLike 삭제 -> 정상")
	public void deleteTest() {
		
		//When
		commentLikeService.delete(commentLike);
		
		//Then
		then(commentLikeRepository).should(times(1)).delete(commentLike);
	}
	
	/*
	 * public boolean existsByUserIdAndCommentId(Long userId, Long commentId)
	 */
	
	@Test
	@DisplayName("User Id, Comment Id로 CommentLike 존재하는 여부 반환 -> true")
	public void existsByUserIdAndCommentIdTrue() {
		//Given
		Long userId = Long.valueOf(1);
		Long commentId = Long.valueOf(1);
		given(commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)).willReturn(true);
		
		//When
		boolean result = commentLikeService.existsByUserIdAndCommentId(userId, commentId);
		
		//Then
		
		assertEquals(true, result);
	}
	
	@Test
	@DisplayName("User Id, Comment Id로 CommentLike 존재하는 여부 반환 -> false")
	public void existsByUserIdAndCommentIdFalse() {
		Long userId = Long.valueOf(1);
		Long commentId = Long.valueOf(1);
		given(commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)).willReturn(false);
		
		//When
		boolean result = commentLikeService.existsByUserIdAndCommentId(userId, commentId);
		
		//Then
		
		assertEquals(false, result);
	}
}
