package com.gunyoung.tmb.dto.jpa;

import com.gunyoung.tmb.enums.TargetType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 운동을 name과 target만 데이터베이스에서 가져올때 바인딩에 사용
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseNameAndTargetDTO {
	private String name;
	private TargetType target;
}
