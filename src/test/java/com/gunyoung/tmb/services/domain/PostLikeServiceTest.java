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

import com.gunyoung.tmb.domain.like.PostLike;
import com.gunyoung.tmb.repos.PostLikeRepository;
import com.gunyoung.tmb.services.domain.like.PostLikeService;

/**
 * PostLikeService 클래스에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class PostLikeServiceTest {
	
	private static final int INIT_POST_LIKE_NUM = 30;
	
	@Autowired
	PostLikeRepository postLikeRepository;
	
	@Autowired
	PostLikeService postLikeService;
	
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
}
