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

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.like.CommentLike;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentLikeRepository;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.UserRepository;
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
	UserRepository userRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
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
	 *  public CommentLike findByUserIdAndCommentId(Long userId, Long commentId)
	 */
	@Test
	@Transactional
	@DisplayName("유저 id와 댓글 id로 찾기 -> 해당 조건 만족의 CommentLike 없음")
	public void findByUserIdAndCommentIdNonExist() {
		//Given
		User user = getUserInstance();
	
		userRepository.save(user);
		
		Comment comment = getCommentInstance();
		
		commentRepository.save(comment);
		
		//When
		CommentLike result = commentLikeService.findByUserIdAndCommentId(user.getId(), comment.getId());
		
		//Then
		assertEquals(result, null);
	}
	
	@Test
	@Transactional
	@DisplayName("유저 id와 댓글 id로 찾기 -> 정상")
	public void findByUserIdAndCommentIdTest() {
		//Given
		CommentLike existCommentLike = commentLikeRepository.findAll().get(0);
		
		User user = getUserInstance();
	
		userRepository.save(user);
		
		Comment comment = getCommentInstance();
		
		commentRepository.save(comment);
		
		// commentLike쪽에서만 객체적 연결해도 해당 테스트에는 무관
		existCommentLike.setUser(user);
		existCommentLike.setComment(comment);
		
		commentLikeRepository.save(existCommentLike);
		
		//When
		
		CommentLike result = commentLikeService.findByUserIdAndCommentId(user.getId(), comment.getId());
		
		//Then
		assertEquals(result != null,true);
		assertEquals(result.getId(),existCommentLike.getId());
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
	 *  public CommentLike saveWithUserAndComment(User user, Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("User와 Comment로 CommentLike 생성 후 저장 -> 정상")
	public void  saveWithUserAndCommentTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		Comment comment = getCommentInstance();
		
		commentRepository.save(comment);
		
		long commentLikeNum = commentLikeRepository.count();
		
		int userCommentLikeNum = user.getCommentLikes().size();
		long userId = user.getId();
		int commentCommentLikeNum = comment.getCommentLikes().size();
		long commentId = comment.getId();
		
		//When
		commentLikeService.saveWithUserAndComment(user, comment);
		
		//Then
		assertEquals(commentLikeNum+1, commentLikeRepository.count());
		assertEquals(userCommentLikeNum +1 , userRepository.findById(userId).get().getCommentLikes().size());
		assertEquals(commentCommentLikeNum +1 , commentRepository.findById(commentId).get().getCommentLikes().size());
		
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
	
	@Test
	@Transactional
	@DisplayName("CommentLike 삭제, 다른 Entity와의 연관성 삭제 -> 정상")
	public void deleteWithRelationWithOtherEntityTest() {
		//Given
		CommentLike commentLike = commentLikeRepository.findAll().get(0);
		
		User user = getUserInstance();
		
		userRepository.save(user);
		
		Comment comment = getCommentInstance();
		
		commentRepository.save(comment);
		
		comment.getCommentLikes().add(commentLike);
		user.getCommentLikes().add(commentLike);
		commentLike.setUser(user);
		commentLike.setComment(comment);
		
		commentLikeRepository.save(commentLike);
		
		int userCommentLikeNum = user.getCommentLikes().size();
		Long userId = user.getId();
		int commentCommentLikeNum = comment.getCommentLikes().size();
		Long commentId = comment.getId();
		long commentLikeNum = commentLikeRepository.count();
		
		//When
		commentLikeService.delete(commentLike);
		
		//Then
		assertEquals(userCommentLikeNum-1,userRepository.findById(userId).get().getCommentLikes().size());
		assertEquals(commentCommentLikeNum-1, commentRepository.findById(commentId).get().getCommentLikes().size());
		assertEquals(commentLikeNum-1 , commentLikeRepository.count());
	}
	
	/*
	 *  public boolean existsByUserIdAndCommentId(Long userId, Long commentId);
	 */
	@Test
	@Transactional
	@DisplayName("User ID, Comment ID로 존재여부 확인 ->정상")
	public void existsByUserIdAndCommentIdTest() {
		//Given
		User user = getUserInstance();
		User userNotHost = getUserInstance();
		userNotHost.setEmail("notHost@test.com");
		userNotHost.setNickName("notHost");
		
		userRepository.save(user);
		userRepository.save(userNotHost);
		
		Comment comment = getCommentInstance();
		commentRepository.save(comment);
		
		CommentLike commentLike = CommentLike.builder()
				.comment(comment)
				.user(user)
				.build();
		
		commentLikeRepository.save(commentLike);
		
		//When
		boolean trueResult = commentLikeService.existsByUserIdAndCommentId(user.getId(), comment.getId());
		boolean falseResult = commentLikeService.existsByUserIdAndCommentId(userNotHost.getId(), comment.getId());
		
		//Then
		assertEquals(trueResult,true);
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
	
	private Comment getCommentInstance() {
		Comment comment = Comment.builder()
				.contents("new comment")
				.writerIp("172.0.0.1")
				.build();
		return comment;
	}
}
