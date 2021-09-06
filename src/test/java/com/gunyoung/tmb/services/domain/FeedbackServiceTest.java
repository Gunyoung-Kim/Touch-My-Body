package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import com.gunyoung.tmb.repos.ExerciseRepository;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;
import com.gunyoung.tmb.util.ExerciseTest;
import com.gunyoung.tmb.util.FeedbackTest;
import com.gunyoung.tmb.util.UserTest;
import com.gunyoung.tmb.util.tag.Integration;
import com.gunyoung.tmb.utils.PageUtil;

/**
 * FeedbackService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class FeedbackServiceTest {
	
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
	}
	
	/*
	 *  public Feedback findById(Long id)
	 */
	@Test
	@Transactional
	@DisplayName("id로 Feedback 찾기 -> 해당 id의 feedback 없음")
	public void findByIdNonExist() {
		//Given
		long nonExistFeedbackId = FeedbackTest.getNonExistFeedbackId(feedbackRepository);
		
		//When
		Feedback result = feedbackService.findById(nonExistFeedbackId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 Feedback 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existFeedbackId = feedback.getId();
		
		//When
		Feedback result = feedbackService.findById(existFeedbackId);
		
		//Then
		assertNotNull(result);
		
	}
	
	/*
	 * public Page<Feedback> findAllByExerciseIdByPage(Long exerciseId, Integer pageNum, int pageSize)
	 */
	@Test
	@Transactional
	@DisplayName("Exercise ID로 Feedback 페이징 처리해서 찾기 -> 정상")
	public void findAllByExerciseIdByPageTest() {
		//Given
		int pageSize = PageUtil.FEEDBACK_FOR_MANAGE_PAGE_SIZE;
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
	 *  public Feedback save(Feedback feedback)
	 */
	
	@Test
	@DisplayName("Feedback 수정하기 -> 정상, 변화 확인")
	public void mergeTestCheckChange() {
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
	public void mergeTestCheckCount() {
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
	public void addTest() {
		//Given
		Feedback newFeedback = FeedbackTest.getFeedbackInstance();
		Long givenFeedbackNum = feedbackRepository.count();
		
		//When
		feedbackService.save(newFeedback);
		
		//Then
		assertEquals(givenFeedbackNum + 1, feedbackRepository.count());
	}
	
	/*
	 * public Feedback saveWithUserAndExercise(User user, Exercise exercise)
	 */
	@Test
	@Transactional
	@DisplayName("User와 Exercise로 Feedback 추가하기 -> 정상, Feedback 개수 추가 확인")
	public void saveWithUserAndExerciseTestCheckCount() {
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
	public void saveWithUserAndExerciseTestCheckWithUser() {
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
	public void saveWithUserAndExerciseTestCheckWithExercise() {
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
	 *  public void delete(Feedback feedback)
	 */
	
	@Test
	@Transactional
	@DisplayName("Feedback 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		Long givenFeedbackNum = feedbackRepository.count();
		
		//When
		feedbackService.delete(feedback);
		
		//Then
		assertEquals(givenFeedbackNum - 1, feedbackRepository.count());
	}	
}
