package com.gunyoung.tmb.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 모든 근육을 분류(TargetType으로)해서 클라이언트에게 전달할떄 사용
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MuscleInfoBySortDTO {
	private String category;
	private List<String> muscleNames;
	
	/**
	 * category, muscleNames를 통해 MusclInfoBySortDTO 생성 후 반환
	 * @param category koreanName of TargetType
	 * @param listOfMuscleNames Muscle name 필드 리스트
	 * @return MuscleInfoBySortDTO
	 * @author kimgun-yeong
	 */
	public static MuscleInfoBySortDTO of(String category, List<String> listOfMuscleNames) {
		return MuscleInfoBySortDTO.builder()
				.category(category)
				.muscleNames(listOfMuscleNames)
				.build();
	}
}
