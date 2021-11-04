package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.error.exceptions.nonexist.LikeNotFoundedException;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.like.PostLikeService;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.PostLikeTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * PostLikeService 클래스에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class PostLikeServiceTest {
	
	@Autowired
	PostLikeRepository postLikeRepository;
	
	@Autowired
	PostLikeService postLikeService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	private PostLike postLike;
	
	@BeforeEach
	void setup() {
		postLike = PostLikeTest.getPostLikeInstance();
		postLikeRepository.save(postLike);
	}
	
	@AfterEach
	void tearDown() {
		postLikeRepository.deleteAll();
	}
	
	/*
	 *   PostLike save(PostLike postLike)
	 *   
	 *   변경할 필드 없어서 패스
	 */
	
	@Test
	@Transactional
	@DisplayName("PostLike 수정 -> 정상")
	void mergeTest() {
		//Given
		
		//When
		postLikeService.save(postLike);
		
		//Then
		
	}
	
	@Test
	@Transactional
	@DisplayName("PostLike 추가 -> 정상")
	void saveTest() {
		//Given
		PostLike newPostLike = PostLikeTest.getPostLikeInstance();
		Long givenPostLikeNum = postLikeRepository.count();
		
		//When
		postLikeService.save(newPostLike);
		
		//Then
		assertEquals(givenPostLikeNum + 1, postLikeRepository.count());
	}
	
	/*
	 *  PostLike saveWithUserAndExercisePost(User user, ExercisePost exercisePost)
	 */

	@Test
	@Transactional
	@DisplayName("User, ExercisePost의 PostLike 추가 -> 정상, PostLike 개수 확인")
	void saveWithUserAndExercisePostTestCheckCount() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		
		long givenPostLikeNum = postLikeRepository.count();
		
		//When
		postLikeService.createAndSaveWithUserAndExercisePost(user, exercisePost);
		
		//Then
		assertEquals(givenPostLikeNum +1 , postLikeRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("User, ExercisePost의 PostLike 추가 -> 정상, User와의 연관관계 확인")
	void saveWithUserAndExercisePostTestCheckWithUser() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		
		//When
		PostLike result = postLikeService.createAndSaveWithUserAndExercisePost(user, exercisePost);
		
		//Then
		assertEquals(user.getId(), result.getUser().getId());
	}
	
	@Test
	@Transactional
	@DisplayName("User, ExercisePost의 PostLike 추가 -> 정상, ExercisePost와의 연관관계 확인")
	void saveWithUserAndExercisePostTestCheckWithExercisePost() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		
		//When
		PostLike result = postLikeService.createAndSaveWithUserAndExercisePost(user, exercisePost);
		
		//Then
		assertEquals(exercisePost.getId(), result.getExercisePost().getId());
	}
	
	/* 
	 *  void delete(PostLike postLike)
	 */
	
	@Test
	@Transactional
	@DisplayName("PostLike 삭제 ->  정상")
	void deleteTest() {
		//Given
		Long givenPostLikeNum = postLikeRepository.count();
		
		//When
		postLikeService.delete(postLike);
		
		//Then
		assertEquals(givenPostLikeNum - 1, postLikeRepository.count());
	}
	
	/*
	 * PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저 Id와 게시글 Id로 PostLike 찾기 -> 해당 PostLike 없음")
	void findByUserIdAndExercisePostIdNonExist() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		Long userId = user.getId();
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		Long exercisePostId = exercisePost.getId();
		
		//When, Then
		assertThrows(LikeNotFoundedException.class, () -> {
			postLikeService.findByUserIdAndExercisePostId(userId, exercisePostId);
		});
	}
	
	@Test
	@Transactional
	@DisplayName("유저 Id와 게시글 Id로 PostLike 찾기 -> 정상")
	void findByUserIdAndExercisePostIdTest() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		
		postLike.setUser(user);
		postLike.setExercisePost(exercisePost);
		
		postLikeRepository.save(postLike);
		
		//When
		PostLike result = postLikeService.findByUserIdAndExercisePostId(user.getId(), exercisePost.getId());
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId);
	 */
	
	@Test
	@Transactional
	@DisplayName("유저 ID와 게시글 ID로 존재여부 확인 ->정상 , true")
	void existsByUserIdAndExercisePostIdTestTrue() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		
		postLike.setUser(user);
		postLike.setExercisePost(exercisePost);
		postLikeRepository.save(postLike);
		
		//When
		boolean result = postLikeService.existsByUserIdAndExercisePostId(user.getId(), exercisePost.getId());
	
		//Then
		assertTrue(result);
	}	
	
	@Test
	@Transactional
	@DisplayName("유저 ID와 게시글 ID로 존재여부 확인 ->정상 , false")
	void existsByUserIdAndExercisePostIdTestFalse() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		ExercisePost exercisePostNotHost =  ExercisePostTest.getExercisePostInstance();
		exercisePostRepository.save(exercisePostNotHost);
		
		postLike.setUser(user);
		postLikeRepository.save(postLike);
		
		//When
		boolean result = postLikeService.existsByUserIdAndExercisePostId(user.getId(), exercisePostNotHost.getId());
	
		//Then
		assertFalse(result);
	}	
}
