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
	private String target;
	private List<String> muscleNames;
}
