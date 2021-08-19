package com.gunyoung.tmb.util;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.enums.TargetType;

/**
 * Test 클래스 전용 Exercise 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class ExerciseTest {
	
	/**
	 * 테스트용 Exercise 인스턴스 반환 <br>
	 * name, target 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Exercise getExerciseInstance(String name, TargetType target) {
		Exercise exercise = Exercise.builder()
				.name(name)
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(target)
				.build();
		
		return exercise;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 Exercise ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistExerciseId(JpaRepository<Exercise, Long> exerciseRepository) {
		Long nonExistExerciseId = Long.valueOf(1);
		
		for(Exercise u : exerciseRepository.findAll()) {
			nonExistExerciseId = Math.max(nonExistExerciseId, u.getId());
		}
		nonExistExerciseId++;
		
		return nonExistExerciseId;
	}
}
