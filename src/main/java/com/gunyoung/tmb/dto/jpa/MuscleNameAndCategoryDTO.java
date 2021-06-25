package com.gunyoung.tmb.dto.jpa;

import com.gunyoung.tmb.enums.TargetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Muscle의 name과 catrgory만 데이터베이스에서 가져오기 위한 DTO
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuscleNameAndCategoryDTO {
	private String name;
	private TargetType category;
}
