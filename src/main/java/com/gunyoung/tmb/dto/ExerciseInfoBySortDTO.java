package com.gunyoung.tmb.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 신체부위에 해당하는 운동들 클라이언트에 보낼떄 사용
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseInfoBySortDTO {
	String target;
	List<String> exerciseNames;
}
