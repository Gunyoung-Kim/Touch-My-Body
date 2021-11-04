package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.FeedbackViewDTO;
import com.gunyoung.tmb.enums.PageSize;
import com.gunyoung.tmb.error.exceptions.nonexist.FeedbackNotFoundedException;
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.testutil.ExerciseTest;
import com.gunyoung.tmb.testutil.FeedbackTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * FeedbackService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
class FeedbackServiceTest {
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExerciseRepository exerciseRepository;
	
	@Autowired
	FeedbackService feedbackService;
	
	private Feedback feedback;
	
	@BeforeEach
	void setup() {
		feedback = FeedbackTest.getFeedbackInstance();
		feedbackRepository.save(feedback);
	}
	
	@AfterEach
	void tearDown() {
		feedbackRepository.deleteAll();
		userRepository.deleteAll();
		exerciseRepository.deleteAll();
	}
	
	/*
	 *  Feedback findById(Long id)
	 */
	@Test
	@DisplayName("id로 Feedback 찾기 -> 해당 id의 feedback 없음")
	void findByIdNonExist() {
		//Given
		long nonExistFeedbackId = FeedbackTest.getNonExistFeedbackId(feedbackRepository);
		
		//When, Then
		assertThrows(FeedbackNotFoundedException.class, () -> {
			feedbackService.findById(nonExistFeedbackId);
		});
	}
	
	@Test
	@DisplayName("id로 Feedback 찾기 -> 정상")
	void findByIdTest() {
		//Given
		Long existFeedbackId = feedback.getId();
		
		//When
		Feedback result = feedbackService.findById(existFeedbackId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 * public FeedbackViewDTO findForFeedbackViewDTOById(Long id);
	 */
	
	@Test
	@DisplayName("ID로 Feedback 찾고 FeedbackViewDTO 생성 후 반환 -> 해당 ID의 Feedback 없음")
	void findForFeedbackViewDTOByIdTestNonExist() {
		//Given
		Long nonExistFeedbackId = FeedbackTest.getNonExistFeedbackId(feedbackRepository);
		
		//When, Then
		assertThrows(FeedbackNotFoundedException.class, () -> {
			feedbackService.findForFeedbackViewDTOById(nonExistFeedbackId);
		});
	}
	
	@Test
	@DisplayName("ID로 Feedback 찾고 FeedbackViewDTO 생성 후 반환 -> 정상")
	void findForFeedbackViewDTOByIdTest() {
		//Given
		User user = UserTest.getUserInstance();
		Exercise exercise = ExerciseTest.getExerciseInstance();
		setNewUserAndExerciseForFeedback(user, exercise);
		Long existFeedbackId = feedback.getId();
		
		//When
		FeedbackViewDTO result = feedbackService.findForFeedbackViewDTOById(existFeedbackId);
		
		//Then
		verifyResultFor_findForFeedbackViewDTOByIdTest(feedback, user, exercise, result);
	}

	private void setNewUserAndExerciseForFeedback(User user, Exercise exercise) {
		userRepository.save(user);
		exerciseRepository.save(exercise);
		
		feedback.setUser(user);
		feedback.setExercise(exercise);
		feedbackRepository.save(feedback);
	}
	
	private void verifyResultFor_findForFeedbackViewDTOByIdTest(Feedback feedback, User user, Exercise exercise,FeedbackViewDTO result) {
		assertEquals(feedback.getId(), result.getId());
		assertEquals(feedback.getTitle(), result.getTitle());
		assertEquals(user.getNickName(), result.getUserNickName());
		assertEquals(exercise.getName(), result.getExerciseName());
		assertEquals(feedback.getCreatedAt(), result.getCreatedAt());
	}
	
	/*
	 * Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize)
	 */
	@Test
	@Transactional
	@DisplayName("Exercise ID로 Feedback 페이징 처리해서 찾기 -> 정상")
	void findAllByExerciseIdByPageTest() {
		//Given
		int pageSize = PageSize.FEEDBACK_FOR_MANAGE_PAGE_SIZE.getSize();
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		FeedbackTest.addNewFeedbacksInDBByNum(10, feedbackRepository);
		
		List<Feedback> feedbacks = feedbackRepository.findAll();
		long givenFeedbackNum = feedbacks.size();
		for(Feedback f: feedbacks) {
			f.setExercise(exercise);
		}
		feedbackRepository.saveAll(feedbacks);
		
		//When
		List<Feedback> result = feedbackService.findAllByExerciseIdByPage(exercise.getId(), 1, pageSize).getContent();
		
		//Then
		assertEquals(Math.min(givenFeedbackNum, pageSize), result.size());
	}
	
	/*
	 *  Feedback save(Feedback feedback)
	 */
	
	@Test
	@DisplayName("Feedback 수정하기 -> 정상, 변화 확인")
	void mergeTestCheckChange() {
		//Given
		String changeTitle = "Changed Title";
		Long feedbackId = feedback.getId();
		feedback.setTitle(changeTitle);
		
		//When
		feedbackService.save(feedback);
		
		//Then
		Feedback result = feedbackRepository.findById(feedbackId).get();
		assertEquals(changeTitle, result.getTitle());
	}
	
	@Test
	@DisplayName("Feedback 수정하기 -> 정상, 개수 변화 없음 확인")
	void mergeTestCheckCount() {
		//Given
		String changeTitle = "Changed Title";
		feedback.setTitle(changeTitle);
		
		long givenFeedbackNum = feedbackRepository.count();
		
		//When
		feedbackService.save(feedback);
		
		//Then
		assertEquals(givenFeedbackNum, feedbackRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Feedback 추가하기 -> 정상")
	void addTest() {
		//Given
		Feedback newFeedback = FeedbackTest.getFeedbackInstance();
		Long givenFeedbackNum = feedbackRepository.count();
		
		//When
		feedbackService.save(newFeedback);
		
		//Then
		assertEquals(givenFeedbackNum + 1, feedbackRepository.count());
	}
	
	/*
	 * Feedback saveWithUserAndExercise(User user, Exercise exercise)
	 */
	@Test
	@Transactional
	@DisplayName("User와 Exercise로 Feedback 추가하기 -> 정상, Feedback 개수 추가 확인")
	void saveWithUserAndExerciseTestCheckCount() {
		//Given
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		long givenFeedbackNum = feedbackRepository.count();
		
		//When
		feedbackService.saveWithUserAndExercise(feedback,user, exercise);
		
		//Then
		assertEquals(givenFeedbackNum + 1, feedbackRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("User와 Exercise로 Feedback 추가하기 -> 정상, User와의 연관관계 확인")
	void saveWithUserAndExerciseTestCheckWithUser() {
		//Given
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		//When
		Feedback result = feedbackService.saveWithUserAndExercise(feedback,user, exercise);
		
		//Then
		assertEquals(user.getId(), result.getUser().getId());
	}
	
	@Test
	@Transactional
	@DisplayName("User와 Exercise로 Feedback 추가하기 -> 정상, Exercise와의 연관관계 확인")
	void saveWithUserAndExerciseTestCheckWithExercise() {
		//Given
		Feedback feedback = FeedbackTest.getFeedbackInstance();
		
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Exercise exercise = ExerciseTest.getExerciseInstance();
		exerciseRepository.save(exercise);
		
		//When
		Feedback result = feedbackService.saveWithUserAndExercise(feedback,user, exercise);
		
		//Then
		assertEquals(exercise.getId(), result.getExercise().getId());
	}
	
	/*
	 *  void delete(Feedback feedback)
	 */
	
	@Test
	@Transactional
	@DisplayName("Feedback 삭제하기 -> 정상")
	void deleteTest() {
		//Given
		Long givenFeedbackNum = feedbackRepository.count();
		
		//When
		feedbackService.delete(feedback);
		
		//Then
		assertEquals(givenFeedbackNum - 1, feedbackRepository.count());
	}	
}
