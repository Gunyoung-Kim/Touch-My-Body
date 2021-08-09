package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
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

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostViewDTO;
import com.gunyoung.tmb.dto.response.PostForCommunityViewDTO;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.services.domain.exercise.ExercisePostServiceImpl;

/**
 * {@link ExercisePostServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExercisePostServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class ExercisePostServiceUnitTest {
	
	@Mock
	ExercisePostRepository exercisePostRepository;
	
	@InjectMocks 
	ExercisePostServiceImpl exercisePostService;
	
	private ExercisePost exercisePost;
	
	@BeforeEach
	void setup() {
		exercisePost = new ExercisePost();
	}
	
	/*
	 * public ExercisePost findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		ExercisePost result = exercisePostService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		ExercisePost result = exercisePostService.findById(exercisePostId);
		
		//Then
		assertEquals(exercisePost, result);
	}
	
	/*
	 * public ExercisePost findWithCommentsById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기, Comments 페치 조인 -> 존재하지 않음")
	public void findWithCommentsByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findWithCommentsById(nonExistId)).willReturn(Optional.empty());
		
		//When
		ExercisePost result = exercisePostService.findWithCommentsById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 ExercisePost 찾기, Comments 페치 조인 -> 정상")
	public void findWithCommentsByIdTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findWithCommentsById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		ExercisePost result = exercisePostService.findWithCommentsById(exercisePostId);
		
		//Then
		assertEquals(exercisePost, result);
	}
	
	/*
	 * public Page<ExercisePost> findAllByUserIdOrderByCreatedAtAsc(Long userId,Integer pageNumber, int pageSize)
	 */
	
	@Test
	@DisplayName("UserID 를 만족하는 ExercisePost들 생성 오래된순으로 페이지 반환 -> 정상")
	public void findAllByUserIdOrderByCreatedAtAscTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;

		//When
		exercisePostService.findAllByUserIdOrderByCreatedAtAsc(userId, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllByUserIdOrderByCreatedAtASCCustom(anyLong(), any(PageRequest.class));
	}
	
	/*
	 * public Page<ExercisePost> findAllByUserIdOrderByCreatedAtDesc(Long userId,Integer pageNumber, int pageSize)
	 */
	
	@Test
	@DisplayName("UserID 를 만족하는 ExercisePost들 생성 최신순으로 페이지 반환 -> 정상")
	public void findAllByUserIdOrderByCreatedAtDescTest() {
		//Given
		Long userId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllByUserIdOrderByCreatedAtDesc(userId, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllByUserIdOrderByCreatedAtDescCustom(anyLong(), any(PageRequest.class));
	}
	
	/*
	 * public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	public void findAllForPostForCommunityViewDTOByPageTest() {
		//Given
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOByPage(pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOByPage(any(PageRequest.class));
	}
	
	/*
	 * public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithKeywordByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("키워드를 만족하는 모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	public void findAllForPostForCommunityViewDTOWithKeywordByPageTest() {
		//Given
		String keyword = "keyword";
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOWithKeywordByPage(keyword, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOWithKeywordByPage(anyString(), any(PageRequest.class));
	}
	
	/*
	 * public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("target을 만족하는 모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	public void findAllForPostForCommunityViewDTOWithTargetByPageTest() {
		//Given
		TargetType target = TargetType.ARM;
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOWithTargetByPage(target, pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOWithTargetByPage(any(TargetType.class), any(PageRequest.class));
	}
	
	/*
	 * public Page<PostForCommunityViewDTO> findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(Integer pageNumber,int pageSize) 
	 */
	
	@Test
	@DisplayName("target과 keyword를 만족하는 모든 ExercisePost로 PostForCommunityViewDTO 생성 후 페이지 반환 -> 정상")
	public void findAllForPostForCommunityViewDTOWithTargetAndKeywordByPageTest() {
		//Given
		TargetType target = TargetType.ARM;
		String keyword = "keyword";
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		exercisePostService.findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(target, keyword,pageNum, pageSize);
		
		//Then
		then(exercisePostRepository).should(times(1)).findAllForPostForCommunityViewDTOWithTargetAndKeywordByPage(any(TargetType.class), anyString(), any(PageRequest.class));
	}
	
	/*
	 * public ExercisePost save(ExercisePost exercisePost)
	 */
	
	@Test
	@DisplayName("ExercisePost 저장 -> 정상")
	public void saveTest() {
		//Given
		given(exercisePostRepository.save(exercisePost)).willReturn(exercisePost);
		
		//When
		ExercisePost result = exercisePostService.save(exercisePost);
		
		//Then
		assertEquals(exercisePost, result);
	}
	
	/*
	 * public ExercisePost saveWithUserAndExercise(ExercisePost exercisePost, User user, Exercise exericse)
	 */
	
	@Test
	@DisplayName("User, Exercise와 연관 관계 추가 후 ExercisePost 저장 -> 저장")
	public void saveWithUserAndExerciseTest() {
		//Given
		User user = new User();
		Exercise exercise = new Exercise();
		
		given(exercisePostRepository.save(exercisePost)).willReturn(exercisePost);
		
		//When
		ExercisePost result = exercisePostService.saveWithUserAndExercise(exercisePost, user, exercise);
		
		//Then
		assertEquals(user, result.getUser());
		assertEquals(exercise, result.getExercise());
	}
	
	/*
	 * public void delete(ExercisePost exercisePost)
	 */
	
	@Test
	@DisplayName("ExercisePost 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		exercisePostService.delete(exercisePost);
		
		//Then
		then(exercisePostRepository).should(times(1)).delete(exercisePost);
	}
	
	/*
	 * public void deleteById(Long id)
	 */
	
	@Test
	@DisplayName("ID를 만족하는 ExercisePost 삭제 -> 해당 ID만족 ExercisePost 없음")
	public void deleteByIdNonExist() {
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
	public void deleteByIdTest() {
		//Given
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		exercisePostService.deleteById(exercisePostId);
		
		//Then
		then(exercisePostRepository).should(times(1)).delete(exercisePost);
	}
	
	/*
	 * public void checkIsMineAndDelete(Long userId, Long exercisePostId)
	 */
	
	@Test
	@DisplayName("User ID, ExercisePost ID 에 해당하는 ExercisePost 있으면 삭제  -> 없었다고 한다")
	public void checkIsMineAndDeleteNonExist() {
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
	public void checkIsMineAndDeleteTest() {
		//Given
		Long userId = Long.valueOf(1);
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findByUserIdAndExercisePostId(userId, exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		exercisePostService.checkIsMineAndDelete(userId, exercisePostId);
		
		//Then
		then(exercisePostRepository).should(times(1)).delete(exercisePost);
	}
	
	/*
	 * public long count()
	 */
	
	@Test
	@DisplayName("모든 ExercisePost의 개수 반환 -> 정상")
	public void countTest() {
		//Given
		long num = 1;
		given(exercisePostRepository.count()).willReturn(num);
		
		//When
		long result = exercisePostService.count();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * public long countWithUserId(Long userId)
	 */
	
	@Test
	@DisplayName("해당 User ID 만족하는 ExercisePost 개수 반환 -> 정상")
	public void countWithUserIdTest() {
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
	 * public long countWithTitleAndContentsKeyword(String keyword)
	 */
	
	@Test
	@DisplayName("Title, Contents 검색 키워드 만족하는 ExercisePost 개수 반환 -> 정상")
	public void countWithTitleAndContentsKeywordTest() {
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
	 * public long countWithTarget(TargetType target)
	 */
	
	@Test
	@DisplayName("Exercise의 target 만족하는 ExercisePost 개수 반환 -> 정상")
	public void countWithTargetTest() {
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
	 * public long countWithTargetAndKeyword(TargetType target, String keyword)
	 */
	
	@Test
	@DisplayName("Title, Contents 검색 키워드 와 Exercise의 target 만족하는 ExercisePost 개수 반환 -> 정상")
	public void countWithTargetAndKeywordTest() {
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
	 * public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostId(Long id)
	 */
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 ID의 ExercisePost 없을때")
	public void getExercisePostViewDTOWithExercisePostIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findForExercisePostViewDTOById(nonExistId)).willReturn(Optional.empty());
		
		//When
		ExercisePostViewDTO result = exercisePostService.getExercisePostViewDTOWithExercisePostId(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 정상")
	public void getExercisePostViewDTOWithExercisePostIdTest() {
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
	 * public ExercisePostViewDTO getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(Long id)
	 */
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 ID의 ExercisePost 없을때, 반환 값 테스트")
	public void getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNumNonExistReturn() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		ExercisePostViewDTO result = exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 해당 ID의 ExercisePost 없을때, ExercisePost 상태 유지 테스트")
	public void getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNumNonExistStable() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(exercisePostRepository.findById(nonExistId)).willReturn(Optional.empty());
				
		//When
		exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(nonExistId);
		
		//Then
		then(exercisePostRepository).should(never()).save(any(ExercisePost.class));
	}
	
	@Test
	@DisplayName("ExercisePost id로 ExercisePost 가져와서 이를 통해 ExercisePostViewDTO 생성 및 반환 -> 정상")
	public void getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNumTest() {
		//Given
		int beforeViewNum = 1;
		exercisePost.setViewNum(beforeViewNum);
		
		Long exercisePostId = Long.valueOf(1);
		given(exercisePostRepository.findById(exercisePostId)).willReturn(Optional.of(exercisePost));
		
		//When
		exercisePostService.getExercisePostViewDTOWithExercisePostIdAndIncreaseViewNum(exercisePostId);
		
		//Then
		then(exercisePostRepository).should(times(1)).save(exercisePost);
		assertEquals(beforeViewNum + 1, exercisePost.getViewNum());
	}
}

