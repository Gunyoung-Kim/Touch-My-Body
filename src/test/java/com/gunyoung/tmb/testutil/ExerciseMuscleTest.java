package com.gunyoung.tmb.testutil;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

/**
 * Test 클래스 전용 ExerciseMuscle 엔티티 관련 유틸리티 클래스
 */
public class ExerciseMuscleTest {
	
	public static final String DEFAULT_EXERCISE_MUSCLE_NAME = "muscleName";
	
	/**
	 * 테스트용 ExerciseMuscle 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static ExerciseMuscle getExerciseMuscleInstance() {
		return getExerciseMuscleInstance(DEFAULT_EXERCISE_MUSCLE_NAME, true);
	}
	
	/**
	 * 테스트용 ExerciseMuscle 인스턴스 반환 <br>
	 * muscleName, isMain 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static ExerciseMuscle getExerciseMuscleInstance(String muscleName, boolean isMain) {
		ExerciseMuscle exerciseMuscle = ExerciseMuscle.builder()
				.muscleName(muscleName)
				.isMain(isMain)
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
