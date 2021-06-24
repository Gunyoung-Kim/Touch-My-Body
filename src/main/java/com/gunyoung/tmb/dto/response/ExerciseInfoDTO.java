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
public class ExerciseInfoDTO {
	private String name;
	private String description;
	private String caution;
	private String movement;
	private String target;
	private String mainMuscle;
	private String subMuscle;
	
	public static ExerciseInfoDTO of(Exercise exercise) { 
		ExerciseInfoDTO dto = ExerciseInfoDTO.builder()
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
				mainMuscleBuilder.append(muscle.getMuscleName());
			} else {
				subMuscleBuilder.append(muscle.getMuscleName());
			}
		}
		
		dto.setMainMuscle(mainMuscleBuilder.toString());
		dto.setSubMuscle(subMuscleBuilder.toString());
		
		return dto;
	}
}
