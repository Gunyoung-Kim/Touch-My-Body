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
import com.gunyoung.tmb.repos.CommentRepository;
import com.gunyoung.tmb.services.domain.exercise.CommentService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CommentServiceTest {

	private static final int INIT_COMMENT_NUM = 30;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentService commentService;
	
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
}
