package com.gunyoung.tmb.dto.reqeust;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.enums.TargetType;
import com.gunyoung.tmb.error.codes.TargetTypeErrorCode;
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
public class SaveMuscleDTO {
	private String name;
	private String category;
	
	/**
	 * Muscle 인스턴스 정보를 통해 SaveMuscleDTO 생성
	 * @author kimgun-yeong
	 */
	public static SaveMuscleDTO of(Muscle muscle) {
		SaveMuscleDTO dto = SaveMuscleDTO.builder()
				.name(muscle.getName())
				.category(muscle.getCategory().getKoreanName())
				.build();
		
		return dto;
	}
	
	/**
	 * SaveMuscleDTO을 통해 Muscle 객체 생성 후 반환
	 * @throws TargetTypeNotFoundedException SaveMuscleDTO category에 해당하는 TargetType KoreanName 없을 때
	 * @author kimgun-yeong
	 */
	public Muscle createMuscle() throws TargetTypeNotFoundedException{
		String categoryKoreanName = this.category;
		TargetType newMusclesCategory = TargetType.getFromKoreanName(categoryKoreanName);
		if(newMusclesCategory == null) {
			throw new TargetTypeNotFoundedException(TargetTypeErrorCode.TARGET_TYPE_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Muscle muscle = Muscle.builder()
				.name(this.name)
				.category(newMusclesCategory)
				.build();
		return muscle;
	}
	
	/**
	 * SaveMuscleDTO를 통해 Muscle 업데이트 후 반환 
	 * @throws TargetTypeNotFoundedException dto category에 해당하는 TargetType KoreanName 없을 때 
	 */
	public static Muscle updateMuscleBySaveMuscleDTO(Muscle muscle, SaveMuscleDTO dto) throws TargetTypeNotFoundedException{
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
