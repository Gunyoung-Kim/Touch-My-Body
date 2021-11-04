package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.data.domain.PageRequest;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.exceptions.nonexist.ExercisePostNotFoundedException;
import com.gunyoung.tmb.precondition.PreconditionViolationException;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostServiceImpl;
import com.gunyoung.tmb.services.domain.like.PostLikeService;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.UserTest;

/**
 * {@link ExercisePostServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExercisePostServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class ExercisePostServiceUnitTest {
	
	@Mock
	ExercisePostRepository exercisePostRepository;
	
	@Mock
	CommentService commentService;
	
	@Mock
	PostLikeService postLikeService;
	
	@InjectMocks 
	ExercisePostServiceImpl exercisePostService;
	
	private ExercisePost exercisePost;
	
	@BeforeEach
	void setup() {
		exercisePost = ExercisePost.builder()
				.id(Long.valueOf(85))
				.build();
	}
	
	/*
	 * ExercisePost findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기 -> 존재하지 않음")
	void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostService.findById(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		ExercisePost result = exercisePostService.findById(exercisePostId);
		
		//Then
		assertEquals(exercisePost, result);
	}
	
	/*
	 * ExercisePost findWithPostLikesById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기, PostLikes 페치 조인 -> 존재하지 않음")
	void findWithPostLikesByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findWithPostLikesById(nonExistId)).willReturn(Optional.empty());
		
		//When, Then
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostService.findWithPostLikesById(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기, PostLikes 페치 조인 -> 정상")
	void findWithPostLikesByIdTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findWithPostLikesById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		ExercisePost result = exercisePostService.findWithPostLikesById(exercisePostId);
		
		//Then
		assertEquals(exercisePost, result);
	}
	
	/*
	 * ExercisePost findWithCommentsById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기, Comments 페치 조인 -> 존재하지 않음")
	void findWithCommentsByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findWithCommentsById(nonExistId)).willReturn(Optional.empty());
		
		//When, Then 
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostService.findWithCommentsById(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기, Comments 페치 조인 -> 정상")
	void findWithCommentsByIdTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findWithCommentsById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		ExercisePost result = exercisePostService.findWithCommentsById(exercisePostId);
		
		//Then
		assertEquals(exercisePost, result);
	}
	
	/*
	 * Page<ExercisePost> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNumber, int pageSize)
	 */
	
	@Test
	@DisplayName("UserID 를 만족하는 ExercisePost들 생성 오래된순으로 페이지 반환 -> pageNumber이 1보다 작음")
	void findAllByUserIdOrderByCreatedAtAscTestPageNumberIsLessThanOne() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 0;
		int pageSize = 1;
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId, pageNum, pageSize);
		});
	}
	
	@Test
	@DisplayName("UserID 를 만족하는 ExercisePost들 생성 오래된순으로 페이지 반환 -> 정상")
	void findAllByUserIdOrderByCreatedAtAscTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;

		//When
		exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllByUserIdOrderByCreatedAtAscInPage(anyLong(), any(PageRequest.class));
	}
	
	/*
	 * Page<ExercisePost> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNumber, int pageSize)
	 */
	
	@Test
	@DisplayName("UserID 를 만족하는 ExercisePost들 생성 최신순으로 페이지 반환 -> pageNumber이 1보다 작음")
	void findAllByUserIdOrderByCreatedAtDescTestPageNumberIsLessThanOne() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 0;
		int pageSize = 1;
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId, pageNum, pageSize);
		});
	}
	
	@Test
	@DisplayName("UserID 를 만족하는 ExercisePost들 생성 최신순으로 페이지 반환 -> 정상")
	void findAllByUserIdOrderByCreatedAtDescTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllByUserIdOrderByCreatedAtDescInPage(anyLong(), any(PageRequest.class));
	}
	
	/*
	 * Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> pageSize가 0보다 작음")
	void findAllForPostForCommunityViewDTOByPageTestPageSizeIsLessThanZero() {
		//Given
		int pageNum = 2;
		int pageSize = 0;
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exercisePostService.findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(pageNum, pageSize);
		});
	}
	
	@Test
	@DisplayName("모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	void findAllForPostForCommunityViewDTOByPageTest() {
		//Given
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOOderByCreatedAtDESCByPage(pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOOrderByCreatedAtDescInPage(any(PageRequest.class));
	}
	
	/*
	 * Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("키워드를 만족하는 모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	void findAllForPostForCommunityViewDTOWithKeywordByPageTest() {
		//Given
		String keyword = "keyword";
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOWithKeywordInPage(anyString(), any(PageRequest.class));
	}
	
	/*
	 * Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("target을 만족하는 모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	void findAllForPostForCommunityViewDTOWithTargetByPageTest() {
		//Given
		TargetType target = TargetType.ARM;
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(target, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOWithTargetInPage(any(TargetType.class), any(PageRequest.class));
	}
	
	/*
	 * Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("target과 keyword를 만족하는 모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	void findAllForPostForCommunityViewDTOWithTargetAndKeywordByPageTest() {
		//Given
		TargetType target = TargetType.ARM;
		String keyword = "keyword";
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(target, keyword,pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOWithTargetAndKeywordInPage(any(TargetType.class), anyString(), any(PageRequest.class));
	}
	
	/*
	 * ExercisePost save(ExercisePost exercisePost)
	 */
	
	@Test
	@DisplayName("ExercisePost 저장 -> 정상")
	void saveTest() {
		//Given
		given(exercisePostRepository.save(exercisePost)).willReturn(exercisePost);
		
		//When
		ExercisePost result = exercisePostService.save(exercisePost);
		
		//Then
		assertEquals(exercisePost, result);
	}
	
	/*
	 * ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse)
	 */
	
	@Test
	@DisplayName("User, Exercise와 연관 관계 추가 후 ExercisePost 저장 -> 입력 exercisePost == null")
	void saveWithUserAndExerciseTestNullExercisePost() {
		//Given
		User user = UserTest.getUserInstance();
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exercisePostService.saveWithUserAndExercise(null, user, exercise);
		});
	}
	
	@Test
	@DisplayName("User, Exercise와 연관 관계 추가 후 ExercisePost 저장 -> 입력 user == null")
	void saveWithUserAndExerciseTestNullUser() {
		//Given
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exercisePostService.saveWithUserAndExercise(exercisePost, null, exercise);
		});
	}
	
	@Test
	@DisplayName("User, Exercise와 연관 관계 추가 후 ExercisePost 저장 -> 입력 exercise == null")
	void saveWithUserAndExerciseTestNullExercise() {
		//Given
		User user = UserTest.getUserInstance();
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exercisePostService.saveWithUserAndExercise(exercisePost, user, null);
		});
	}
	
	@Test
	@DisplayName("User, Exercise와 연관 관계 추가 후 ExercisePost 저장 -> 저장")
	void saveWithUserAndExerciseTest() {
		//Given
		User user = UserTest.getUserInstance();
		Exercise exercise = ExerciseTest.getExerciseInstance();
		
		given(exercisePostRepository.save(exercisePost)).willReturn(exercisePost);
		
		//When
		ExercisePost result = exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
		
		//Then
		assertEquals(user, result.getUser());
		assertEquals(exercise, result.getExercise());
	}
	
	/*
	 * void delete(ExercisePost exercisePost)
	 */
	
	@Test
	@DisplayName("ExercisePost 삭제 -> 실패, exercisePost == null")
	void deleteTestExercisePostNull() {
		//Given
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			exercisePostService.delete(null);
		});
	}
	
	@Test
	@DisplayName("ExercisePost 삭제 -> 정상, check ExercisePostRepository")
	void deleteTestCheckExercisePostRepo() {
		//Given
		
		//When
		exercisePostService.delete(exercisePost);
		
		//Then
		then(exercisePostRepository).should(times(1)).deleteByIdInQuery(exercisePost.getId());
	}
	
	@Test
	@DisplayName("ExercisePost 삭제 -> 정상, Check PostLikeService") 
	void deleteTestCheckPostLikeService(){
		//Given
		Long exercisePostId = Long.valueOf(85);
		exercisePost.setId(exercisePostId);
		
		//When
		exercisePostService.delete(exercisePost);
		
		//Then
		then(postLikeService).should(times(1)).deleteAllByExercisePostId(exercisePostId);
	}
	
	@Test
	@DisplayName("ExercisePost 삭제 -> 정상, Check CommentService") 
	void deleteTestCheckCommentService(){
		//Given
		Long exercisePostId = Long.valueOf(85);
		exercisePost.setId(exercisePostId);
		
		//When
		exercisePostService.delete(exercisePost);
		
		//Then
		then(commentService).should(times(1)).deleteAllByExercisePostId(exercisePostId);
	}
	
	/*
	 * void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("ID를 만족하는 ExercisePost 삭제 -> 해당 ID만족 ExercisePost 없음")
	void deleteByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		exercisePostService.deleteById(nonExistId);
		
		//Then
		then(exercisePostRepository).should(never()).delete(any(ExercisePost.class));
	}
	
	@Test
	@DisplayName("ID를 만족하는 ExercisePost 삭제 -> 정상")
	void deleteByIdTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		exercisePostService.deleteById(exercisePostId);
		
		//Then
		then(exercisePostRepository).should(times(1)).deleteByIdInQuery(exercisePost.getId());
	}
	
	/*
	 * void checkIsMineAndDelete(Long userId, Long exercisePostId)
	 */
	
	@Test
	@DisplayName("User ID, ExercisePost ID 에 해당하는 ExercisePost 있으면 삭제  -> 없었다고 한다")
	void checkIsMineAndDeleteNonExist() {
		//Given
		Long userId = Long.valueOf(1);
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findByUserIdAndExercisePostId(userId, nonExistId)).willReturn(Optional.empty());
		
		//When
		exercisePostService.checkIsMineAndDelete(userId, nonExistId);
		
		//Then
		then(exercisePostRepository).should(never()).delete(any(ExercisePost.class));
	}
	
	@Test
	@DisplayName("User ID, ExercisePost ID 에 해당하는 ExercisePost 있으면 삭제  -> 정상")
	void checkIsMineAndDeleteTest() {
		//Given
		Long userId = Long.valueOf(1);
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findByUserIdAndExercisePostId(userId, exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		exercisePostService.checkIsMineAndDelete(userId, exercisePostId);
		
		//Then
		then(exercisePostRepository).should(times(1)).deleteByIdInQuery(exercisePost.getId());
	}
	
	/*
	 * void deleteAllByUserId(Long userId)
	 */
	
	@Test
	@DisplayName("User ID에 해당하는 ExerciePost들 일괄 삭제 -> 정상, check only ExercisePostRepo")
	void deleteAllByUserIdTestCheckExercisePostRepo() {
		//Given
		Long userId = Long.valueOf(74);
		List<ExercisePost> exercisePosts = new ArrayList<>();
		given(exercisePostRepository.findAllByUserIdInQuery(userId)).willReturn(exercisePosts);
		
		//When
		exercisePostService.deleteAllByUserId(userId);
		
		//Then
		then(exercisePostRepository).should(times(1)).deleteAllByUserIdInQuery(userId);
	}
	
	@Test
	@DisplayName("User ID에 해당하는 ExerciePost들 일괄 삭제 -> 정상, check OneToMany Domain Services")
	void deleteAllByUserIdTestCheckOneToManyDelete() {
		//Given
		List<ExercisePost> exercisePosts = new ArrayList<>();
		Long exercisePostsId = Long.valueOf(72);
		int givenExercisePostNum = 35;
		for(int i=0;i<givenExercisePostNum;i++) {
			ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
			exercisePost.setId(exercisePostsId);
			exercisePosts.add(exercisePost);
		}
		
		Long userId = Long.valueOf(74);
		given(exercisePostRepository.findAllByUserIdInQuery(userId)).willReturn(exercisePosts);
		
		//When
		exercisePostService.deleteAllByUserId(userId);
		
		//Then
		verifyServices_for_deleteAllByUserIdTestCheckOneToManyDelete(givenExercisePostNum, exercisePostsId);
	}
	
	private void verifyServices_for_deleteAllByUserIdTestCheckOneToManyDelete(int givenExercisePostNum, Long exercisePostsId) {
		then(postLikeService).should(times(givenExercisePostNum)).deleteAllByExercisePostId(exercisePostsId);
		then(commentService).should(times(givenExercisePostNum)).deleteAllByExercisePostId(exercisePostsId);
	}
	
	/*
	 * void deleteAllByExerciseId(Long exerciseId)
	 */
	
	@Test
	@DisplayName("Exercise ID에 해당하는 ExerciePost들 일괄 삭제 -> 정상, check only ExercisePostRepo")
	void deleteAllByExerciseIdTestCheckExercisePostRepo() {
		//Given
		Long exerciseId = Long.valueOf(74);
		List<ExercisePost> exercisePosts = new ArrayList<>();
		given(exercisePostRepository.findAllByExerciseIdInQuery(exerciseId)).willReturn(exercisePosts);
		
		//When
		exercisePostService.deleteAllByExerciseId(exerciseId);
		
		//Then
		then(exercisePostRepository).should(times(1)).deleteAllByExerciseIdInQuery(exerciseId);
	}
	
	@Test
	@DisplayName("Exercise ID에 해당하는 ExerciePost들 일괄 삭제 -> 정상, check OneToMany Domain Services")
	void deleteAllByExerciseIdTestCheckOneToManyDelete() {
		//Given
		List<ExercisePost> exercisePosts = new ArrayList<>();
		Long exercisePostsId = Long.valueOf(48);
		int givenExercisePostNum = 6;
		for(int i=0;i<givenExercisePostNum;i++) {
			ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
			exercisePost.setId(exercisePostsId);
			exercisePosts.add(exercisePost);
		}
		
		Long exerciseId = Long.valueOf(74);
		given(exercisePostRepository.findAllByExerciseIdInQuery(exerciseId)).willReturn(exercisePosts);
		
		//When
		exercisePostService.deleteAllByExerciseId(exerciseId);
		
		//Then
		verifyServices_for_deleteAllByExerciseIdTestCheckOneToManyDelete(givenExercisePostNum, exercisePostsId);
	}
	
	private void verifyServices_for_deleteAllByExerciseIdTestCheckOneToManyDelete(int givenExercisePostNum, Long exercisePostsId) {
		then(postLikeService).should(times(givenExercisePostNum)).deleteAllByExercisePostId(exercisePostsId);
		then(commentService).should(times(givenExercisePostNum)).deleteAllByExercisePostId(exercisePostsId);
	}
	
	/*
	 * long count()
	 */
	
	@Test
	@DisplayName("모든 ExercisePost의 개수 반환 -> 정상")
	void countTest() {
		//Given
		long num = 1;
		given(exercisePostRepository.count()).willReturn(num);
		
		//When
		long result = exercisePostService.count();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * long countWithUserId(Long userId)
	 */
	
	@Test
	@DisplayName("해당 User ID 만족하는 ExercisePost 개수 반환 -> 정상")
	void countWithUserIdTest() {
		//Given
		long num = 1;
		Long userId = Long.valueOf(1);
		given(exercisePostRepository.countWithUserId(userId)).willReturn(num);
		
		//When
		long result = exercisePostService.countWithUserId(userId);
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * long countWithTitleAndContentsKeyword(String keyword)
	 */
	
	@Test
	@DisplayName("Title, Contents 검색 키워드 만족하는 ExercisePost 개수 반환 -> 정상")
	void countWithTitleAndContentsKeywordTest() {
		//Given
		long num = 1;
		String keyword = "keyword";
		given(exercisePostRepository.countWithTitleAndContentsKeyword(keyword)).willReturn(num);
		
		//When
		long result = exercisePostService.countWithTitleAndContentsKeyword(keyword);
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * long countWithTarget(TargetType target)
	 */
	
	@Test
	@DisplayName("Exercise의 target 만족하는 ExercisePost 개수 반환 -> 정상")
	void countWithTargetTest() {
		//Given
		long num = 1;
		TargetType target = TargetType.ARM;
		given(exercisePostRepository.countWithTarget(target)).willReturn(num);
		
		//When
		long result = exercisePostService.countWithTarget(target);
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * long countWithTargetAndKeyword(TargetType target, String keyword)
	 */
	
	@Test
	@DisplayName("Title, Contents 검색 키워드 와 Exercise의 target 만족하는 ExercisePost 개수 반환 -> 정상")
	void countWithTargetAndKeywordTest() {
		//Given
		long num = 1;
		String keyword = "keyword";
		TargetType target = TargetType.ARM;
		
		given(exercisePostRepository.countWithTargetAndKeyword(target, keyword)).willReturn(num);
		
		//When
		long result = exercisePostService.countWithTargetAndKeyword(target, keyword);
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id)
	 */
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 ID의 ExercisePost 없을때")
	void getExercisePostViewDTOWithExercisePostIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findForExercisePostViewDTOById(nonExistId)).willReturn(Optional.empty());
		
		//When, Then
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostService.getExercisePostViewDTOWithExercisePostId(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 정상")
	void getExercisePostViewDTOWithExercisePostIdTest() {
		//Given
		ExercisePostViewDTO exercisePostViewDTO = new ExercisePostViewDTO();
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findForExercisePostViewDTOById(exercisePostId)).willReturn(Optional.of(exercisePostViewDTO));
		
		//When
		ExercisePostViewDTO result = exercisePostService.getExercisePostViewDTOWithExercisePostId(exercisePostId);
		
		//Then
		assertEquals(exercisePostViewDTO, result);
	}
	
	/*
	 * ExercisePostViewDTO getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(Long id)
	 */
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 ID의 ExercisePost 없을때, 반환 값 테스트")
	void getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNumNonExistReturn() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When, Then 
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(nonExistId);
		});
	}
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 ID의 ExercisePost 없을때, ExercisePost 상태 유지 테스트")
	void getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNumNonExistStable() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findById(nonExistId)).willReturn(Optional.empty());
				
		//When
		assertThrows(ExercisePostNotFoundedException.class, () -> {
			exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(nonExistId);
		});
		
		//Then
		then(exercisePostRepository).should(never()).save(any(ExercisePost.class));
	}
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 정상")
	void getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNumTest() {
		//Given
		int beforeViewNum = 1;
		exercisePost.setViewNum(beforeViewNum);
		
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		ExercisePostViewDTO dto = ExercisePostTest.getExercisePostViewDTOInstance();
		given(exercisePostRepository.findForExercisePostViewDTOById(exercisePostId)).willReturn(Optional.of(dto));
		
		//When
		exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(exercisePostId);
		
		//Then
		then(exercisePostRepository).should(times(1)).save(exercisePost);
		assertEquals(beforeViewNum + 1, exercisePost.getViewNum());
	}
}

