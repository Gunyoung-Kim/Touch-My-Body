package com.gunyoung.tmb.dto.reqeust;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
import com.gunyoung.tmb.error.exceptions.BusinessException;
import com.gunyoung.tmb.error.exceptions.nonexist.TargetTypeNotFoundedException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Muscle 추가 및 수정 할떄 사용되는 객체
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMuscleDTO {
	private String name;
	private String category;
	
	public static AddMuscleDTO of(Muscle muscle) {
		AddMuscleDTO dto = AddMuscleDTO.builder()
				.name(muscle.getName())
				.category(muscle.getCategory().getKoreanName())
				.build();
		
		return dto;
	}
	
	public static Muscle toMuscle(Muscle muscle, AddMuscleDTO dto) throws BusinessException{
		muscle.setName(dto.getName());
		
		TargetType newMusclesCategory = null;
		
		String category = dto.getCategory();
		
		for(TargetType tt: TargetType.values()) {
			if(tt.getKoreanName().equals(category)) {
				newMusclesCategory = tt;
				break;
			}
		}
		
		if(newMusclesCategory == null) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		muscle.setCategory(newMusclesCategory);
		
		return muscle;
	}
}
