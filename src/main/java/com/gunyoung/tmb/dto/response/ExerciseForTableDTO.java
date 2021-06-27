package com.gunyoung.tmb.dto.response;

import com.gunyoung.tmb.domain.exercise.Exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 운동 정보 화면의 테이블에 보여질 정보들을 담은 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseForTableDTO {
	private Long id;
	private String name;
	private String target;
	
	public static ExerciseForTableDTO of(Exercise exercise) {
		ExerciseForTableDTO dto = ExerciseForTableDTO.builder()
				.id(exercise.getId())
				.name(exercise.getName())
				.target(exercise.getTarget().getKoreanName())
				.build();
		
		return dto;
	}
}
