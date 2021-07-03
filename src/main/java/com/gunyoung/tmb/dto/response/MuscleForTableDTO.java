package com.gunyoung.tmb.dto.response;

import com.gunyoung.tmb.domain.exercise.Muscle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Muscle 객체들 테이블에 보여주기 위해 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuscleForTableDTO {
	private Long id;
	private String name;
	private String category;
	
	/**
	 * Muscle 객체로 MuscleForTableDTO 객체 생성 및 반환하는 메소드
	 * @param muscle
	 * @return
	 */
	public static MuscleForTableDTO of(Muscle muscle) {
		MuscleForTableDTO dto = MuscleForTableDTO.builder()
				.id(muscle.getId())
				.name(muscle.getName())
				.category(muscle.getCategory().getKoreanName())
				.build();
		
		return dto;
	}
}
