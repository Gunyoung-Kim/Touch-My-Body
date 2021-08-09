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

import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.services.domain.like.PostLikeServiceImpl;

/**
 * {@link PostLikeServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) PostLikeServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class PostLikeServiceUnitTest {
	
	@Mock
	PostLikeRepository postLikeRepository;
	
	@InjectMocks 
	PostLikeServiceImpl postLikeService;
	
	private PostLike postLike;
	
	@BeforeEach
	void setup() {
		postLike = PostLike.builder()
				.build();
	}
	
	/*
	 * public PostLike findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 PostLike 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(postLikeRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		PostLike result = postLikeService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 PostLike 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long postLikeId = Long.valueOf(1);
		
		given(postLikeRepository.findById(postLikeId)).willReturn(Optional.of(postLike));
		
		//When
		PostLike result = postLikeService.findById(postLikeId);
				
		//Then
		assertEquals(postLike, result);
	}
	
	/*
	 * public PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId)
	 */
	
	@Test
	@DisplayName("User Id, ExercisePost Id 로 PostLike 찾기 -> 존재하지 않음")
	public void findByUserIdAndExercisePostIdNonExist() {
		//Given
		Long userId = Long.valueOf(1);
		Long exercisePostId = Long.valueOf(1);
		
		given(postLikeRepository.findByUserIdAndExercisePostId(userId,exercisePostId)).willReturn(Optional.empty());
		
		//When
		PostLike result = postLikeService.findByUserIdAndExercisePostId(userId,exercisePostId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("User Id, ExercisePost Id 로 PostLike 찾기 -> 정상")
	public void findByUserIdAndExercisePostIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		Long exercisePostId = Long.valueOf(1);
		
		given(postLikeRepository.findByUserIdAndExercisePostId(userId,exercisePostId)).willReturn(Optional.of(postLike));
		
		//When
		PostLike result = postLikeService.findByUserIdAndExercisePostId(userId,exercisePostId);
				
		//Then
		assertEquals(postLike, result);
	}
	
	/*
	 * public PostLike save(PostLike postLike) 
	 */
	
	@Test
	@DisplayName("PostLike 저장 -> 정상")
	public void saveTest() {
		//Given
		given(postLikeRepository.save(postLike)).willReturn(postLike);
		
		//When
		PostLike result = postLikeService.save(postLike);
		
		//Then
		assertEquals(postLike, result);
	}
	
	/*
	 * public void delete(PostLike postLike)
	 */
	
	@Test
	@DisplayName("PostLike 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		postLikeService.delete(postLike);
		
		//Then
		then(postLikeRepository).should(times(1)).delete(postLike);
	}
	
	/*
	 * public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId)
	 */
	
	@Test
	@DisplayName("User Id, ExercisePost Id로 PostLike 존재 여부 반환 -> 정상")
	public void existsByUserIdAndExercisePostIdTest() {
		//Given
		Long userId = Long.valueOf(1);
		Long exercisePostId = Long.valueOf(1);
		
		boolean isExist = true;
		
		given(postLikeRepository.existsByUserIdAndExercisePostId(userId, exercisePostId)).willReturn(isExist);
		
		//When
		boolean result = postLikeService.existsByUserIdAndExercisePostId(userId, exercisePostId);
		
		//Then
		assertEquals(isExist, result);
	}
}
