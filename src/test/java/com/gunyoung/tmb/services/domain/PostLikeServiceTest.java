package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.like.PostLikeService;

/**
 * PostLikeService 클래스에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@SpringBootTest
public class PostLikeServiceTest {
	
	private static final int INIT_POST_LIKE_NUM = 30;
	
	@Autowired
	PostLikeRepository postLikeRepository;
	
	@Autowired
	PostLikeService postLikeService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@BeforeEach
	void setup() {
		for(int i=1;i<=INIT_POST_LIKE_NUM;i++) {
			PostLike postLike = PostLike.builder().build();
			
			postLikeRepository.save(postLike);
		}
	}
	
	@AfterEach
	void tearDown() {
		postLikeRepository.deleteAll();
	}
	
	/*
	 *  public PostLike findById(Long id)
	 */
	@Test
	@DisplayName("id로 PostLike 찾기 -> 해당 id의 PostLike없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<PostLike> list = postLikeRepository.findAll();
		
		for(PostLike pl: list) {
			maxId = Math.max(maxId, pl.getId());
		}
		
		//When
		PostLike result = postLikeService.findById(maxId+1000);
		
		//Then
		assertEquals(result, null);
	}
	
	@Test
	@DisplayName("id로 PostLike 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existId = postLikeRepository.findAll().get(0).getId();
		
		//When
		PostLike result = postLikeService.findById(existId);
		
		//Then
		assertEquals(result != null, true);
	}
	
	/*
	 *   public PostLike save(PostLike postLike)
	 *   
	 *   변경할 필드 없어서 패스
	 */
	
	@Test
	@Transactional
	@DisplayName("PostLike 수정 -> 정상")
	public void mergerTest() {
		//Given
		PostLike postLike = postLikeRepository.findAll().get(0);
		
		//When
		
		postLikeService.save(postLike);
		
		//Then
		
	}
	
	@Test
	@Transactional
	@DisplayName("PostLike 추가 -> 정상")
	public void saveTest() {
		//Given
		PostLike postLike = PostLike.builder().build();
		Long beforeNum = postLikeRepository.count();
		
		//When
		postLikeService.save(postLike);
		
		//Then
		assertEquals(beforeNum+1, postLikeRepository.count());
	}
	
	/*
	 *  public PostLike saveWithUserAndExercisePost(User user, ExercisePost exercisePost)
	 */

	@Test
	@Transactional
	@DisplayName("User, ExercisePost의 PostLike 추가 -> 정상")
	public void saveWithUserAndExercisePostTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		ExercisePost exercisePost = getExercisePostInstance();
		
		exercisePostRepository.save(exercisePost);
		
		int userPostLikeNum = user.getPostLikes().size();
		Long userId = user.getId();
		int exercisePostPostLikeNum = exercisePost.getPostLikes().size();
		Long exercisePostId = exercisePost.getId();
		
		long postLikeNum = postLikeRepository.count();
		
		
		//When
		postLikeService.saveWithUserAndExercisePost(user, exercisePost);
		
		//Then
		assertEquals(userPostLikeNum +1,userRepository.findById(userId).get().getPostLikes().size());
		assertEquals(exercisePostPostLikeNum +1,exercisePostRepository.findById(exercisePostId).get().getPostLikes().size());
		assertEquals(postLikeNum +1 , postLikeRepository.count());
	}
	
	/* 
	 *  public void delete(PostLike postLike)
	 */
	
	@Test
	@Transactional
	@DisplayName("PostLike 삭제 ->  정상")
	public void deleteTest() {
		//Given
		PostLike postLike = postLikeRepository.findAll().get(0);
		Long beforeNum = postLikeRepository.count();
		
		//When
		postLikeService.delete(postLike);
		
		//Then
		assertEquals(beforeNum-1, postLikeRepository.count());
	}
	
	/*
	 * public PostLike findByUserIdAndExercisePostId(Long userId, Long exercisePostId)
	 */
	
	@Test
	@Transactional
	@DisplayName("유저 Id와 게시글 Id로 PostLike 찾기 -> 해당 PostLike 없음")
	public void findByUserIdAndExercisePostIdNonExist() {
		//Given
		Long nonExistUserId = Long.valueOf(100);
		Long nonExistExercisePostId = Long.valueOf(100);
		
		//when
		PostLike result = postLikeService.findByUserIdAndExercisePostId(nonExistUserId, nonExistExercisePostId);
		
		//Then
		assertEquals(result, null);
		
	}
	
	@Test
	@Transactional
	@DisplayName("유저 Id와 게시글 Id로 PostLike 찾기 -> 정상")
	public void test() {
		//Given
		PostLike postLike = postLikeRepository.findAll().get(0);
		User user = getUserInstance();
		
		userRepository.save(user);
		
		ExercisePost exercisePost = getExercisePostInstance();
		
		exercisePostRepository.save(exercisePost);
		
		postLike.setUser(user);
		postLike.setExercisePost(exercisePost);
		user.getPostLikes().add(postLike);
		exercisePost.getPostLikes().add(postLike);
		
		postLikeRepository.save(postLike);
		
		//When
		PostLike result = postLikeService.findByUserIdAndExercisePostId(user.getId(), exercisePost.getId());
		
		//Then
		assertEquals(result != null, true);
	
	}
	
	/*
	 *  public boolean existsByUserIdAndExercisePostId(Long userId, Long exercisePostId);
	 */
	
	@Test
	@Transactional
	@DisplayName("유저 ID와 게시글 ID로 존재여부 확인 ->정상")
	public void  existsByUserIdAndExercisePostIdTest() {
		//Given
		User user = getUserInstance();
		userRepository.save(user);
		ExercisePost exercisePost = getExercisePostInstance();
		exercisePostRepository.save(exercisePost);
		
		ExercisePost exercisePostNotHost =  getExercisePostInstance();
		exercisePostRepository.save(exercisePostNotHost);
		
		List<PostLike> postLikes = postLikeRepository.findAll();
		
		PostLike existPostLike = postLikes.get(0);
		
		existPostLike.setUser(user);
		existPostLike.setExercisePost(exercisePost);
		
		postLikeRepository.save(existPostLike);
		
		//When
		boolean trueResult = postLikeService.existsByUserIdAndExercisePostId(user.getId(), exercisePost.getId());
		boolean falseResult = postLikeService.existsByUserIdAndExercisePostId(user.getId(), exercisePostNotHost.getId());
	
		//Then
		assertEquals(trueResult, true);
		assertEquals(falseResult,false);
	}
	
	
	/*
	 * 
	 */
	
	private User getUserInstance() {
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("first")
				.lastName("last")
				.role(RoleType.USER)
				.nickName("nickName")
				.build();
		return user;
	}
	
	private ExercisePost getExercisePostInstance() {
		ExercisePost exercisePost = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		return exercisePost;
	}
	
}
