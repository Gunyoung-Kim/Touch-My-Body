package com.gunyoung.tmb.dto.jpa;

import com.gunyoung.tmb.enums.TargetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 운동을 target으로 분류해서 반환하기 위해 데이터베이스에서 관련 데이터 가져올때 사용
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseSortDTO {
	private String name;
	private TargetType target;
}
