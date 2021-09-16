package com.gunyoung.tmb.dto.response;

import java.util.List;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에게 운동 정보 페이지에서 사용할 운동 정보 전달 할때 사용
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseForInfoViewDTO {
	private Long id;
	private String name;
	private String description;
	private String caution;
	private String movement;
	private String target;
	private String mainMuscle;
	private String subMuscle;
	
	/**
	 * Exercise를 통해 ExerciseForInfoViewDTO 객체 생성 및 반환
	 * @author kimgun-yeong
	 */
	public static ExerciseForInfoViewDTO of(Exercise exercise) {
		ExerciseForInfoViewDTO dto = ExerciseForInfoViewDTO.builder()
				.id(exercise.getId())
				.name(exercise.getName())
				.description(exercise.getDescription())
				.caution(exercise.getCaution())
				.movement(exercise.getMovement())
				.target(exercise.getTarget().getKoreanName())
				.build();
		
		List<ExerciseMuscle> muscles = exercise.getExerciseMuscles();
		StringBuilder mainMuscleBuilder = new StringBuilder();
		StringBuilder subMuscleBuilder = new StringBuilder();
		for(ExerciseMuscle muscle: muscles) {
			if(muscle.isMain()) {
				mainMuscleBuilder.append(muscle.getMuscleName()+".");
			} else {
				subMuscleBuilder.append(muscle.getMuscleName()+".");
			}
		}
		String mainMusclesString = removeDotSuffixFromString(mainMuscleBuilder.toString());
		String subMusclesString = removeDotSuffixFromString(subMuscleBuilder.toString());
		
		dto.setMainMuscle(mainMusclesString);
		dto.setSubMuscle(subMusclesString);
		
		return dto;
	}
	
	private static String removeDotSuffixFromString(String str) {
		if(str.endsWith(".")) {
			return str.substring(0, str.length()-1);
		}
		return str;
	}
}
