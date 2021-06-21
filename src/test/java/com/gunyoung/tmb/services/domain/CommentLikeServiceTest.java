package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.services.domain.like.CommentLikeService;

/**
 * CommentLikeService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CommentLikeServiceTest {
	
	private static final int INIT_COMMENT_LIKE_NUM = 30; 
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
	@Autowired
	CommentLikeService commentLikeService;
	
	@BeforeEach
	void setup() {
		for(int i=1;i<=INIT_COMMENT_LIKE_NUM;i++) {
			CommentLike commentLike = CommentLike.builder()
												 .build();
			
			commentLikeRepository.save(commentLike);
		}
	}
	
	@AfterEach
	void tearDown() {
		commentLikeRepository.deleteAll();
	}
	
	/*
	 *   public Comment findById(Long id)
	 */
	
	@Test
	@Transactional
	@DisplayName("id로 commentLike 찾기 -> 해당 id의 commentLike 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<CommentLike> list = commentLikeRepository.findAll();
		
		for(CommentLike cl: list) {
			maxId = Math.max(maxId, cl.getId());
		}
		
		//When
		CommentLike result = commentLikeService.findById(maxId+1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 commentLike 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existId = commentLikeRepository.findAll().get(0).getId();
		
		//When
		CommentLike result = commentLikeService.findById(existId);
		
		//Then
		assertEquals(result != null, true);
	}
	
	/*
	 *   public Comment save(Comment comment)
	 *   
	 *   아직 변경할 필드 없어서 패스
	 */
	
	@Test
	@Transactional
	@DisplayName("commentLike 수정하기 -> 정상")
	public void mergerTest() {
		//Given
		CommentLike commentLike = commentLikeRepository.findAll().get(0);
		
		//When
		
		commentLikeService.save(commentLike);
		
		//Then
		
	}
	
	@Test
	@Transactional
	@DisplayName("commentLike 추가하기 -> 정상")
	public void saveTest() {
		//Given
		CommentLike commentLike = CommentLike.builder().build();
		Long beforeNum = commentLikeRepository.count();
		
		//When
		commentLikeService.save(commentLike);
		
		//Then
		assertEquals(beforeNum+1, commentLikeRepository.count());
	}
	
	/*
	 *  public void delete(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("commentLike 삭제 -> 정상")
	public void deleteTest() {
		//Given
		CommentLike commentLike = commentLikeRepository.findAll().get(0);
		Long beforeNum = commentLikeRepository.count();
		
		//When
		commentLikeService.delete(commentLike);
		
		//Then
		assertEquals(beforeNum-1, commentLikeRepository.count());
	}
}
