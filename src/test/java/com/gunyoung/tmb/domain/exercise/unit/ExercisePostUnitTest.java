package com.gunyoung.tmb.domain.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.testutil.ExercisePostTest;

/**
* {@link ExercisePost} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) ExercisePost only
* @author kimgun-yeong
*
*/
class ExercisePostUnitTest {
	
	/*
	 *  String toString()
	 */
	
	@Test
	@DisplayName("toString Test -> 정상") 
	void toStringTest() {
		//Given
		Long exercisePostId = Long.valueOf(85);
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		exercisePost.setId(exercisePostId);
		
		//When
		String result = exercisePost.toString();
		
		//Then
		verifyString_for_toStringTest(exercisePost, result);
	}
	
	private void verifyString_for_toStringTest(ExercisePost exercisePost, String result) {
		assertEquals("[ id = " + exercisePost.getId() + ", title = " + exercisePost.getTitle() + ", contents = " + exercisePost.getContents() + ", viewNum = " + exercisePost.getViewNum() + " ]", result);
	}
	
	/*
	 * void increaseViewNum()
	 */
	
	@Test
	@DisplayName("조회수 증가 -> 정상")
	void increaseViewNumTest() {
		//Given
		ExercisePost exercisePost = ExercisePostTest.getExercisePostInstance();
		int beforeViewNum = exercisePost.getViewNum();
		
		//When
		exercisePost.increaseViewNum();
		
		//Then
		assertEquals(beforeViewNum + 1, exercisePost.getViewNum());
	}
}
