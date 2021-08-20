package com.gunyoung.tmb.util;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

/**
 * Test 클래스 전용 ExerciseMuscle 엔티티 관련 유틸리티 클래스
 */
public class ExerciseMuscleTest {
	
	/**
	 * 테스트용 ExerciseMuscle 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static ExerciseMuscle getExerciseMuscleInstance() {
		ExerciseMuscle exerciseMuscle = ExerciseMuscle.builder()
				.muscleName("muscleName")
				.isMain(true)
				.build();
		
		return exerciseMuscle;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 ExerciseMuscle ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistExerciseMuscleId(JpaRepository<ExerciseMuscle, Long> exerciseMuscleRepository) {
		Long nonExistId = Long.valueOf(1);
		
		for(ExerciseMuscle em: exerciseMuscleRepository.findAll()) {
			nonExistId = Math.max(nonExistId, em.getId());
		}
		nonExistId++;
		
		return nonExistId;
	}
}
