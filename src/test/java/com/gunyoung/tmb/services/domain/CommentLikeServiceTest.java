package com.gunyoung.tmb.services.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.like.CommentLikeService;
import com.gunyoung.tmb.testutil.CommentLikeTest;
import com.gunyoung.tmb.testutil.CommentTest;
import com.gunyoung.tmb.testutil.UserTest;
import com.gunyoung.tmb.testutil.tag.Integration;

/**
 * CommentLikeService에 대한 테스트 클래스 <br>
 * Spring의 JpaRepository의 정상 작동을 가정으로 두고 테스트 진행
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class CommentLikeServiceTest {
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentLikeService commentLikeService;
	
	private CommentLike commentLike;
	
	
	@BeforeEach
	void setup() {
		commentLike = CommentLikeTest.getCommentLikeInstance();
		commentLikeRepository.save(commentLike);
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
		Long nonExistCommentLikeId = CommentLikeTest.getNonExistCommentLikeId(commentLikeRepository);
		
		//When
		CommentLike result = commentLikeService.findById(nonExistCommentLikeId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("id로 commentLike 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existCommentLikeId = commentLike.getId();
		
		//When
		CommentLike result = commentLikeService.findById(existCommentLikeId);
		
		//Then
		assertNotNull(result);
	}
	
	/*
	 *  public CommentLike findByUserIdAndCommentId(Long userId, Long commentId)
	 */
	@Test
	@Transactional
	@DisplayName("유저 id와 댓글 id로 찾기 -> 해당 조건 만족의 CommentLike 없음")
	public void findByUserIdAndCommentIdNonExist() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Comment comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
		
		//When
		CommentLike result = commentLikeService.findByUserIdAndCommentId(user.getId(), comment.getId());
		
		//Then
		assertNull(result);
	}
	
	@Test
	@Transactional
	@DisplayName("유저 id와 댓글 id로 찾기 -> 정상")
	public void findByUserIdAndCommentIdTest() {
		//Given
		User user = UserTest.getUserInstance();
	
		userRepository.save(user);
		
		Comment comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
		
		// commentLike쪽에서만 객체적 연결해도 해당 테스트에는 무관
		commentLike.setUser(user);
		commentLike.setComment(comment);
		
		commentLikeRepository.save(commentLike);
		
		//When
		
		CommentLike result = commentLikeService.findByUserIdAndCommentId(user.getId(), comment.getId());
		
		//Then
		assertNotNull(result);
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
		
		//When
		commentLikeService.save(commentLike);
		//Then
		
	}
	
	@Test
	@Transactional
	@DisplayName("commentLike 추가하기 -> 정상")
	public void saveTest() {
		//Given
		CommentLike newCommentLike = CommentLikeTest.getCommentLikeInstance();
		Long givenCommentLikeNum = commentLikeRepository.count();
		
		//When
		commentLikeService.save(newCommentLike);
		
		//Then
		assertEquals(givenCommentLikeNum+1, commentLikeRepository.count());
	}
	
	/* 
	 *  public CommentLike createAndSaveWithUserAndComment(User user, Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("User와 Comment로 CommentLike 생성 후 저장 -> 정상, 개수 추가 확인")
	public void  createAndSaveWithUserAndCommentTestCheckCount() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Comment comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
		
		long givenCommentLikeNum = commentLikeRepository.count();
		
		//When
		commentLikeService.createAndSaveWithUserAndComment(user, comment);
		
		//Then
		assertEquals(givenCommentLikeNum+1, commentLikeRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("User와 Comment로 CommentLike 생성 후 저장 -> 정상, User와 연관 관계 추가 확인")
	public void  createAndSaveWithUserAndCommentTestCheckUser() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Comment comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
		
		//When
		CommentLike result = commentLikeService.createAndSaveWithUserAndComment(user, comment);
		
		//Then
		assertEquals(user, result.getUser());
	}
	
	@Test
	@Transactional
	@DisplayName("User와 Comment로 CommentLike 생성 후 저장 -> 정상, Comment와 연관 관계 추가 확인")
	public void  createAndSaveWithUserAndCommentTestCheckComment() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		Comment comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
		
		//When
		CommentLike result = commentLikeService.createAndSaveWithUserAndComment(user, comment);
		
		//Then
		assertEquals(comment, result.getComment());
	}
	
	/*
	 *  public void delete(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("commentLike 삭제 -> 정상")
	public void deleteTest() {
		//Given
		Long givenCommentLikeNum = commentLikeRepository.count();
		
		//When
		commentLikeService.delete(commentLike);
		
		//Then
		assertEquals(givenCommentLikeNum-1, commentLikeRepository.count());
	}
	
	/*
	 *  public boolean existsByUserIdAndCommentId(Long userId, Long commentId);
	 */
	@Test
	@Transactional
	@DisplayName("User ID, Comment ID로 존재여부 확인 ->정상 , True")
	public void existsByUserIdAndCommentIdTestTrue() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		User userNotHost = UserTest.getUserInstance();
		userNotHost.setEmail("notHost@test.com");
		userNotHost.setNickName("notHost");
		userRepository.save(userNotHost);
		
		Comment comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
		
		CommentLike commentLike = CommentLike.builder()
				.comment(comment)
				.user(user)
				.build();
		
		commentLikeRepository.save(commentLike);
		
		//When
		boolean trueResult = commentLikeService.existsByUserIdAndCommentId(user.getId(), comment.getId());
		
		//Then
		assertTrue(trueResult);
	}
	
	@Test
	@Transactional
	@DisplayName("User ID, Comment ID로 존재여부 확인 ->정상, False")
	public void existsByUserIdAndCommentIdTestFalse() {
		//Given
		User user = UserTest.getUserInstance();
		userRepository.save(user);
		
		User userNotHost = UserTest.getUserInstance();
		userNotHost.setEmail("notHost@test.com");
		userNotHost.setNickName("notHost");
		userRepository.save(userNotHost);
		
		Comment comment = CommentTest.getCommentInstance();
		commentRepository.save(comment);
		
		CommentLike commentLike = CommentLike.builder()
				.comment(comment)
				.user(user)
				.build();
		
		commentLikeRepository.save(commentLike);
		
		//When
		boolean falseResult = commentLikeService.existsByUserIdAndCommentId(userNotHost.getId(), comment.getId());
		
		assertFalse(falseResult);
	}
}
