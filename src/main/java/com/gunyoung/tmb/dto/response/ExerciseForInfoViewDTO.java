package com.gunyoung.tmb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에게 운동 정보 페이지에서 사용할 운동 정보 전달 할때 사용
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseForInfoViewDTO {
	private Long id;
	private String name;
	private String description;
	private String caution;
	private String movement;
	private String target;
	private String mainMuscle;
	private String subMuscle;
}
