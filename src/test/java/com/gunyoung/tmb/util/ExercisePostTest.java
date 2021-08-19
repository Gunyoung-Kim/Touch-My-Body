package com.gunyoung.tmb.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.ExercisePost;

/**
 * Test 클래스 전용 ExercisePost 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class ExercisePostTest {
	
	/**
	 * 테스트용 ExercisePost 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static ExercisePost getExercisePostInstance() {
		ExercisePost ep = ExercisePost.builder()
				.title("title")
				.contents("contents")
				.build();
		return ep;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 ExercisePost ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistExercisePostId(JpaRepository<ExercisePost, Long> exercisePostRepository) {
		Long nonExistExercisePostId = Long.valueOf(1);
		
		for(ExercisePost u : exercisePostRepository.findAll()) {
			nonExistExercisePostId = Math.max(nonExistExercisePostId, u.getId());
		}
		nonExistExercisePostId++;
		
		return nonExistExercisePostId;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveExercisePostDTO} 에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * exerciseName value 커스터마이징 가능 
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getSaveExercisePostDTOMap(String exerciseName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", "title");
		map.add("contents", "contents");
		map.add("exerciseName", exerciseName);
		
		return map;
	}
}
