package com.gunyoung.tmb.services.domain.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.data.domain.PageRequest;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.services.domain.exercise.FeedbackServiceImpl;

/**
 * {@link FeedbackServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) FeedbackServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class FeedbackServiceUnitTest {
	@Mock
	FeedbackRepository feedbackRepository;
	
	@InjectMocks 
	FeedbackServiceImpl feedbackService;
	
	private Feedback feedback;
	
	@BeforeEach
	void setup() {
		feedback = Feedback.builder().build();
	}
	
	/*
	 * public Feedback findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Feedback 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(feedbackRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Feedback result = feedbackService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Feedback 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long feedbackId = Long.valueOf(1);
		given(feedbackRepository.findById(feedbackId)).willReturn(Optional.of(feedback));
		
		//When
		Feedback result = feedbackService.findById(feedbackId);
		
		//Then
		assertEquals(feedback, result);
	}
	
	/*
	 * public FeedbackViewDTO findForFeedbackViewDTOById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Feedback 찾고 FeedbackViewDTO 생성 후 반환 -> 해당 ID의 Feedback 없을때")
	public void findForFeedbackViewDTOByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		given(feedbackRepository.findForFeedbackViewDTOById(nonExistId)).willReturn(Optional.empty());
		
		//When
		FeedbackViewDTO result = feedbackService.findForFeedbackViewDTOById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Feedback 찾고 FeedbackViewDTO 생성 후 반환 -> 정상")
	public void findForFeedbackViewDTOByIdTest() {
		//Given
		FeedbackViewDTO feedbackViewDTO = FeedbackViewDTO.builder().build();
		Long feedbackId = Long.valueOf(1);
		given(feedbackRepository.findForFeedbackViewDTOById(feedbackId)).willReturn(Optional.of(feedbackViewDTO));
		
		//When
		FeedbackViewDTO result = feedbackService.findForFeedbackViewDTOById(feedbackId);
		
		//Then
		assertEquals(feedbackViewDTO, result);
	}
	
	/*
	 * public Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize)
	 */
	
	@Test
	@DisplayName("Exercise ID를 만족하는 Feedback들 페이지 반환 -> 정상")
	public void findAllByExerciseIdByPageTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		feedbackService.findAllByExerciseIdByPage(exerciseId, pageNum, pageSize);
		
		//Then
		then(feedbackRepository).should(times(1)).findAllByExerciseIdByPage(anyLong(), any(PageRequest.class));
	}
	
	/*
	 * public Page<FeedbackManageListDTO> findAllForFeedbackManageListDTOByExerciseIdByPage(Long exerciseId,
	 *		Integer pageNum, int pageSize)
	 */
	
	@Test
	@DisplayName("Exercise ID를 만족하는 Feedback 찾고 FeedbackManageListDTO들 생성해서 페이지 반환")
	public void findAllForFeedbackManageListDTOByExerciseIdByPageTest() {
		//Given
		Long exerciseId = Long.valueOf(1);
		int pageNum = 1;
		int pageSize = 1;
		
		//When
		feedbackService.findAllForFeedbackManageListDTOByExerciseIdByPage(exerciseId, pageNum, pageSize);
		
		//Then
		then(feedbackRepository).should(times(1)).findAllForFeedbackManageListDTOByExerciseIdByPage(anyLong(), any(PageRequest.class));
	}
	
	/*
	 * public Feedback save(Feedback feedback)
	 */
	
	@Test
	@DisplayName("Feedback 생성 및 수정 -> 정상")
	public void saveTest() {
		//Given
		given(feedbackRepository.save(feedback)).willReturn(feedback);
		
		//When
		Feedback result = feedbackService.save(feedback);
		
		//Then
		assertEquals(feedback, result);
	}
	
	/*
	 * public Feedback saveWithUserAndExercise(Feedback feedback,User user, Exercise exercise)
	 */
	
	@Test
	@DisplayName("User와 Exercise객체를 인자로 받아 Feedback 객체 생성 및 연관 관계 설정 후 저장 -> 정상")
	public void saveWithUserAndExerciseTest() {
		//Given
		User user = User.builder().build();
		Exercise exercise = Exercise.builder().build();
		
		given(feedbackRepository.save(feedback)).willReturn(feedback);
		
		//When
		Feedback result = feedbackService.saveWithUserAndExercise(feedback, user, exercise);
		
		//Then
		assertEquals(user, result.getUser());
		assertEquals(exercise, result.getExercise());
	}
	
	/*
	 * public void delete(Feedback feedback)
	 */
	
	@Test
	@DisplayName("Feedback 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		feedbackService.delete(feedback);
		
		//Then
		then(feedbackRepository).should(times(1)).delete(feedback);
	}
	
	/*
	 *  public void deleteAllByUserId(Long userId)
	 */
	
	@Test
	@DisplayName("User ID로 Feedback 일괄 삭제 -> 정상, check Repo") 
	public void deleteAllByUserIdTestCheckRepo() {
		//Given
		Long userId = Long.valueOf(53);
		
		//When
		feedbackService.deleteAllByUserId(userId);
		
		//Then
		then(feedbackRepository).should(times(1)).deleteAllByUserIdInQuery(userId);
	}
	
	/*
	 *  public void deleteAllByExerciseId(Long exerciseId)
	 */
	
	@Test
	@DisplayName("Exercise ID로 Feedback 일괄 삭제 -> 정상, check Repo") 
	public void deleteAllByExerciseIdTestCheckRepo() {
		//Given
		Long exerciseId = Long.valueOf(53);
		
		//When
		feedbackService.deleteAllByExerciseId(exerciseId);
		
		//Then
		then(feedbackRepository).should(times(1)).deleteAllByExerciseIdInQuery(exerciseId);
	}
	
	/*
	 * public long countByExerciseId(Long exerciseId)
	 */
	
	@Test
	@DisplayName("Exercise ID를 만족하는 Feedback들 개수 반환 -> 정상")
	public void countByExerciseIdTest() {
		//Given
		long num = 1;
		Long exerciseId = Long.valueOf(1);
		given(feedbackRepository.countByExerciseId(exerciseId)).willReturn(num);
		
		//When
		long result = feedbackService.countByExerciseId(exerciseId);
		
		//Then
		assertEquals(num, result);
	}
}
