package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Feedback;
import com.gunyoung.tmb.repos.FeedbackRepository;
import com.gunyoung.tmb.services.domain.exercise.FeedbackService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class FeedbackServiceTest {

	private static final int INIT_FEEDBACK_NUM = 30;
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	FeedbackService feedbackService;
	
	@BeforeEach
	void setup() {
		List<Feedback> list = new ArrayList<>();
		for(int i=1;i<=INIT_FEEDBACK_NUM;i++) {
			Feedback feedback = Feedback.builder()
									 .title("title" +i)
									 .content("content" +i)
									 .build();
									 
			list.add(feedback);
		}
		feedbackRepository.saveAll(list);
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
		long maxId = -1;
		List<Feedback> list = feedbackRepository.findAll();
		
		for(Feedback c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		Feedback result = feedbackService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 Feedback 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Feedback feedback = feedbackRepository.findAll().get(0);
		Long id = feedback.getId();
		
		//When
		Feedback result = feedbackService.findById(id);
		
		//Then
		
		assertEquals(result != null, true);
		
	}
	
	/*
	 *  public Feedback save(Feedback feedback)
	 */
	
	@Test
	@Transactional
	@DisplayName("Feedback 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		Feedback existFeedback = feedbackRepository.findAll().get(0);
		Long id = existFeedback.getId();
		existFeedback.setTitle("Changed Title");
		
		//When
		feedbackService.save(existFeedback);
		
		//Then
		Feedback result = feedbackRepository.findById(id).get();
		assertEquals(result.getTitle(),"Changed Title");
	}
	
	@Test
	@Transactional
	@DisplayName("Feedback 추가하기 -> 정상")
	public void saveTest() {
		//Given
		Feedback newFeedback = Feedback.builder()
									.title("New Title")
									.content("New contents")
									.build();
		Long beforeNum = feedbackRepository.count();
		
		//When
		feedbackService.save(newFeedback);
		
		//Then
		assertEquals(beforeNum+1,feedbackRepository.count());
	}
	
	/*
	 *  public void delete(Feedback feedback)
	 */
	
	@Test
	@Transactional
	@DisplayName("Feedback 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		Feedback existFeedback = feedbackRepository.findAll().get(0);
		Long beforeNum = feedbackRepository.count();
		
		//When
		feedbackService.delete(existFeedback);
		
		//Then
		assertEquals(beforeNum-1,feedbackRepository.count());
	}
}
