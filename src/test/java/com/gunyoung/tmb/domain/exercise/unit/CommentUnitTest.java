package com.gunyoung.tmb.domain.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.Comment;
import com.gunyoung.tmb.testutil.CommentTest;

/**
* {@link Comment} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) Comment only
* @author kimgun-yeong
*
*/
public class CommentUnitTest {
	
	/*
	 *  public String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	public void toStringTest() {
		//Given
		Long commentId = Long.valueOf(25);
		Comment comment = CommentTest.getCommentInstance();
		comment.setId(commentId);
		
		//When
		String result = comment.toString();
		
		//Then
		verifyString_for_toStringTest(comment, result);
	}
	
	private void verifyString_for_toStringTest(Comment comment, String result) {
		assertEquals("[ id = " + comment.getId() + ", writerIp = " + comment.getWriterIp() + ", contents = " + comment.getContents() + ", isAnonymous = " + comment.isAnonymous() +" ]", result);
	}
}
