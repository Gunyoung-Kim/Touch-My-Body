package com.gunyoung.tmb.dto.reqeust;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에서 운동 종류 추가를 위해 요청 보낼때 사용
 * @author kimgun-yeong
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddExerciseDTO {
	private String name;
	private String descriptoin;
	private String caution;
	private String movement;
	private String target;
	
	@Builder.Default
	private List<String> mainMuscles = new ArrayList<>();
	
	@Builder.Default
	private List<String> subMuscles = new ArrayList<>();
	
}
