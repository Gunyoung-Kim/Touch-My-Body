package com.gunyoung.tmb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 유저가 특정 날짜에 운동했는지 여부를 클라이언트에 전달하기 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExerciseIsDoneDTO {
	private int date;
	private boolean isDone;
}
