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
import com.gunyoung.tmb.enums.RoleType;
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.repos.ExercisePostRepository;
import com.gunyoung.tmb.repos.UserRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentService;

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
		
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("first")
				.lastName("last")
				.role(RoleType.USER)
				.nickName("nickName")
				.build();
		
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
		
		User user = User.builder()
				.email("test@test.com")
				.password("abcd1234")
				.firstName("first")
				.lastName("last")
				.role(RoleType.USER)
				.nickName("nickName")
				.build();
		
		userRepository.save(user);
		
		ExercisePost exercisePost = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		
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
}
