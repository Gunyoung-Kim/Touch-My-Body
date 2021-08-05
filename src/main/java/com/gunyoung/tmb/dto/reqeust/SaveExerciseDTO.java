package com.gunyoung.tmb.dto.reqeust;

import java.util.ArrayList;
import java.util.List;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.domain.exercise.ExerciseMuscle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에서 운동 종류 추가 및 수정을 위해 요청 보낼때 사용
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveExerciseDTO {
	private String name;
	private String description;
	private String caution;
	private String movement;
	private String target;
	
	@Builder.Default
	private List<String> mainMuscles = new ArrayList<>();
	
	@Builder.Default
	private List<String> subMuscles = new ArrayList<>();
	
	/**
	 * Exercise 객체를 통해 SaveExerciseDTO 객체 생성 및 반환하는 메소드 
	 * @param exercise
	 * @return
	 */
	public static SaveExerciseDTO of(Exercise exercise) {
		SaveExerciseDTO dto = SaveExerciseDTO.builder()
				.name(exercise.getName())
				.description(exercise.getDescription())
				.caution(exercise.getCaution())
				.movement(exercise.getMovement())
				.target(exercise.getTarget().getKoreanName())
				.build();
		
		for(ExerciseMuscle em : exercise.getExerciseMuscles()) {
			if(em.isMain()) {
				dto.getMainMuscles().add(em.getMuscleName());
			} else {
				dto.getSubMuscles().add(em.getMuscleName());
			}
		}
		
		return dto;
	}
}
