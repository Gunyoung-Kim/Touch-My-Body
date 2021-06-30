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

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForPostViewDTO;
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentService;
import com.gunyoung.tmb.utils.PageUtil;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CommentServiceTest {

	private static final int INIT_COMMENT_NUM = 30;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExercisePostRepository exercisePostRepository;
	
	@BeforeEach
	void setup() {
		List<Comment> list = new ArrayList<>();
		for(int i=1;i<=INIT_COMMENT_NUM;i++) {
			Comment comment = Comment.builder()
									 .writerIp("171.0.0.1")
									 .contents(i+"comment")
									 .build();
			list.add(comment);
		}
		commentRepository.saveAll(list);
	}
	
	@AfterEach
	void tearDown() {
		commentRepository.deleteAll();
	}
	
	/*
	 *  public Comment findById(Long id)
	 */
	@Test
	@DisplayName("id로 Comment 찾기 -> 해당 id의 comment 없음")
	public void findByIdNonExist() {
		//Given
		long maxId = -1;
		List<Comment> list = commentRepository.findAll();
		
		for(Comment c: list) {
			maxId = Math.max(maxId, c.getId());
		}
		
		//When
		Comment result = commentService.findById(maxId+ 1000);
		
		//Then
		assertEquals(result,null);
	}
	
	@Test
	@DisplayName("id로 Comment 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long existId = commentRepository.findAll().get(0).getId();
		
		//When
		Comment result = commentService.findById(existId);
		
		//Then
		assertEquals(result != null, true);
	}
	
	/*
	 *  public List<Comment> findByExercisePostId(Long postId)
	 */
	@Test
	@Transactional
	@DisplayName("ExercisePost id로 Comment들 찾기 ->정상")
	public void findByExercisePostIdTest() {
		//Given
		ExercisePost exercisePost = getExercisePostInstance();
		
		exercisePostRepository.save(exercisePost);
		
		Long exercisePostId = exercisePost.getId();
		
		List<Comment> comments = commentRepository.findAll();
		for(Comment c: comments) {
			c.setExercisePost(exercisePost);
		}
		
		commentRepository.saveAll(comments);
		
		//When
		List<Comment> result = commentService.findAllByExercisePostId(exercisePostId);
		
		//Then
		assertEquals(result.size(),INIT_COMMENT_NUM);
	}
	
	/*
	 * public List<Comment> findAllByUserIdOrderByCreatedAtASC(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 오래된순서대로 ->정상")
	public void findAllByUserIdOrderByCreatedAtASCTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		List<Comment> comments = commentRepository.findAll();
		
		for(Comment c: comments) {
			c.setUser(user);
		}
		
		commentRepository.saveAll(comments);
		
		//When
		
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtASC(user.getId(),1,PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertEquals(result.size(),Math.min(INIT_COMMENT_NUM, PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE));
		assertEquals(result.get(0).getCreatedAt().isBefore(result.get(1).getCreatedAt()),true);
	}
	
	/*
	 *  public List<Comment> findAllByUserIdOrderByCreatedAtDESC(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID로 Comment 찾기 최신순서대로 ->정상")
	public void findAllByUserIdOrderByCreatedAtDESCTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		List<Comment> comments = commentRepository.findAll();
		
		for(Comment c: comments) {
			c.setUser(user);
		}
		
		commentRepository.saveAll(comments);
		
		//When
		List<Comment> result = commentService.findAllByUserIdOrderByCreatedAtDESC(user.getId(),1,PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE).getContent();
		
		//Then
		assertEquals(result.size(),Math.min(INIT_COMMENT_NUM, PageUtil.COMMENT_FOR_MANAGE_PAGE_SIZE));
		assertEquals(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()),true);
	}
	
	/*
	 *  public Comment save(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("Comment 수정하기 -> 정상")
	public void mergeTest() {
		//Given
		Comment existComment = commentRepository.findAll().get(0);
		Long id = existComment.getId();
		existComment.setContents("changed Contents");
		
		//When
		commentService.save(existComment);
		
		//Then
		Comment result = commentRepository.findById(id).get();
		assertEquals(result.getContents(),"changed Contents");
	}
	
	@Test
	@Transactional
	@DisplayName("Comment 추가하기 -> 정상")
	public void saveTest() {
		//Given
		Comment newComment = Comment.builder()
									.contents("new Contents")
									.writerIp("0.0.0.1")
									.build();
		Long beforeNum = commentRepository.count();
		
		//When
		commentService.save(newComment);
		
		//Then
		assertEquals(beforeNum+1,commentRepository.count());
	}
	
	/*
	 * public Comment saveWithUserAndExercisePost(User user, ExercisePost exercisePost)
	 */
	
	@Test
	@Transactional
	@DisplayName("User, ExercisePost의 Comment 추가 -> 정상")
	public void saveWithUserAndExercisePostTest() {
		//Given
		Comment comment = Comment.builder()
				 .writerIp("171.0.0.1")
				 .contents("contents")
				 .isAnonymous(true)
				 .build();
		
		User user = getUserInstance();
		
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		
		exercisePostRepository.save(exercisePost);
		
		int userCommentNum = user.getComments().size();
		Long userId = user.getId();
		int exercisePostCommentNum = exercisePost.getComments().size();
		Long exercisePostId = exercisePost.getId();
		
		long commentNum = commentRepository.count();
		
		//When
		commentService.saveWithUserAndExercisePost(comment,user, exercisePost);
		
		//Then
		assertEquals(userCommentNum +1,userRepository.findById(userId).get().getComments().size());
		assertEquals(exercisePostCommentNum +1,exercisePostRepository.findById(exercisePostId).get().getComments().size());
		assertEquals(commentNum +1 , commentRepository.count());
	}
	
	/*
	 *  public void delete(Comment comment)
	 */
	
	@Test
	@Transactional
	@DisplayName("Comment 삭제하기 -> 정상")
	public void deleteTest() {
		//Given
		Comment existComment = commentRepository.findAll().get(0);
		Long beforeNum = commentRepository.count();
		
		//When
		commentService.delete(existComment);
		
		//Then
		assertEquals(beforeNum-1,commentRepository.count());
	}
	
	@Test
	@Transactional
	@DisplayName("Comment 삭제하기, 다른 entity와의 연관성 삭제 -> 정상")
	public void deleteWithRelationWithOtherEntityTest() {
		//Given
		Comment comment = commentRepository.findAll().get(0);
		
		User user = getUserInstance();
		
		userRepository.save(user);
		
		ExercisePost exercisePost = getExercisePostInstance();
		
		exercisePostRepository.save(exercisePost);
		
		comment.setUser(user);
		comment.setExercisePost(exercisePost);
		user.getComments().add(comment);
		exercisePost.getComments().add(comment);
		
		commentRepository.save(comment);
		
		int userCommentNum = user.getComments().size();
		Long userId = user.getId();
		int exercisePostCommentNum = exercisePost.getComments().size();
		Long exercisePostId = exercisePost.getId();
		
		long commentNum = commentRepository.count();
		
		//When
		commentService.delete(comment);
		
		//Then
		assertEquals(userCommentNum -1,userRepository.findById(userId).get().getComments().size());
		assertEquals(exercisePostCommentNum -1,exercisePostRepository.findById(exercisePostId).get().getComments().size());
		assertEquals(commentNum-1, commentRepository.count());
		
	}
	
	/*
	 * public long countByUserId(Long userId)
	 */
	@Test
	@Transactional
	@DisplayName("User ID를 만족하는 Comment 들 개수 가져오기 -> 정상")
	public void countByUserIdTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		List<Comment> comments = commentRepository.findAll();
		
		for(Comment c: comments) {
			c.setUser(user);
		}
		
		commentRepository.saveAll(comments);
		
		Long allUserId = user.getId();
		
		//When
		
		long result = commentService.countByUserId(allUserId);
		
		//Then
		
		assertEquals(result,INIT_COMMENT_NUM);
	}
	
	/*
	 *  public List<CommentForPostViewDTO> getCommentForCommentForPostViewDTOsByExercisePostId(Long postId)
	 */
	
	@Test
	@Transactional
	@DisplayName("해당 exercisePost id 를 만족하는 Comment 객체들을 CommentForPostViewDTO로 변환해서 반환 -> 정상")
	public void getCommentForCommentForPostViewDTOsByExercisePostIdTest() {
		//Given
		User user = getUserInstance();
		
		userRepository.save(user);
		
		ExercisePost exercisePost = getExercisePostInstance();
		
		exercisePostRepository.save(exercisePost);
		
		Long exercisePostId = exercisePost.getId();
		
		List<Comment> comments = commentRepository.findAll();
		for(Comment c: comments) {
			c.setExercisePost(exercisePost);
			c.setUser(user);
		}
		
		commentRepository.saveAll(comments);
		
		//When
		List<CommentForPostViewDTO> result = commentService.getCommentForPostViewDTOsByExercisePostId(exercisePostId);
		
		//Then
		assertEquals(result.size(),INIT_COMMENT_NUM);
	}
	
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
	
	private ExercisePost getExercisePostInstance() {
		ExercisePost exercisePost = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		
		return exercisePost;
	}
}
