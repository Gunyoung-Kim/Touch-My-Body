package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.CommentForManageViewDTO;
import com.gunyoung.tmb.util.CommentTest;
import com.gunyoung.tmb.util.UserTest;

/**
 * {@link CommentForManageViewDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) CommentForManageViewDTO only
 * @author kimgun-yeong
 *
 */
public class CommentForManageViewDTOUnitTest {
	
	/*
	 * public static List<CommentForManageViewDTO> of(Iterable<Comment> comments, User user)
	 */
	
	@Test
	@DisplayName("Comment들과 User를 통해 CommentForManageViewDTO 리스트 반환 -> 정상")
	public void ofCommentsAndUserTest() {
		//Given
		int givenCommentNum = 62;
		Iterable<Comment> comments = getComments(givenCommentNum);
		User user = UserTest.getUserInstance();
		
		//When
		List<CommentForManageViewDTO> result = CommentForManageViewDTO.of(comments, user);
		
		//Then
		assertEquals(givenCommentNum, result.size());
	}
	
	private Iterable<Comment> getComments(int givenCommentNum) {
		List<Comment> comments = new ArrayList<>();
		for(int i=0;i<givenCommentNum;i++) {
			Comment comment = CommentTest.getCommentInstance();
			comments.add(comment);
		}
		return comments;
	}
}
