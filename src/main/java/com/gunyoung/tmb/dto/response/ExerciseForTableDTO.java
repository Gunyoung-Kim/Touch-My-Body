package com.gunyoung.tmb.dto.response;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * Exercise 인스턴스를 통해 ExerciseForTableDTO 객체 생성 후 반환
	 * @author kimgun-yeong
	 */
	public static ExerciseForTableDTO of(Exercise exercise) {
		ExerciseForTableDTO dto = ExerciseForTableDTO.builder()
				.id(exercise.getId())
				.name(exercise.getName())
				.target(exercise.getTarget().getKoreanName())
				.build();
		
		return dto;
	}
	
	/**
	 * Exercise 컬렉션을 통해 ExerciseForTableDTO 리스트 생성 후 반환
	 * @author kimgun-yeong
	 */
	public static List<ExerciseForTableDTO> of(Iterable<Exercise> exercises) {
		List<ExerciseForTableDTO> dtos = new ArrayList<>();
		for(Exercise e: exercises) {
			dtos.add(of(e));
		}
		
		return dtos;
	}
}
