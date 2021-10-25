package com.gunyoung.tmb.dto.response.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.domain.exercise.ExercisePost;
import com.gunyoung.tmb.domain.user.User;
import com.gunyoung.tmb.dto.response.ExercisePostForManageViewDTO;
import com.gunyoung.tmb.testutil.ExercisePostTest;
import com.gunyoung.tmb.testutil.UserTest;

/**
 * {@link ExercisePostForManageViewDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ExercisePostForManageViewDTO only
 * @author kimgun-yeong
 *
 */
class ExercisePostForManageViewDTOUnitTest {
	
	/*
	 * static List<ExercisePostForManageViewDTO> of(Iterable<ExercisePost> exercisePosts, User user) 
	 */
	
	@Test
	@DisplayName("ExercisePost 컬렉션과 User를 통해 ExercisePostForManageViewDTO 리스트 반환 -> 정상")
	void ofListAndUserTest() {
		//Given
		int givenExercisePostNum = 15;
		Iterable<ExercisePost> exercisePosts =  getExercisePosts(givenExercisePostNum);
		User user = UserTest.getUserInstance();
		
		//When
		List<ExercisePostForManageViewDTO> result = ExercisePostForManageViewDTO.of(exercisePosts, user);
		
		//Then
		assertEquals(givenExercisePostNum, result.size());
	}
	
	private Iterable<ExercisePost> getExercisePosts(int exercisePostNum) {
		List<ExercisePost> exercisePosts = new ArrayList<>();
		for(int i=0;i<exercisePostNum;i++) {
			ExercisePost ep = ExercisePostTest.getExercisePostInstance();
			exercisePosts.add(ep);
		}
		return exercisePosts;
	}
}
