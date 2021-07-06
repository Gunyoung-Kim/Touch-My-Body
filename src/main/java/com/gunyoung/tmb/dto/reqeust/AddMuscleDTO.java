package com.gunyoung.tmb.dto.reqeust;

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
}
