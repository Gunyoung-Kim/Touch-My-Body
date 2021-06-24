package com.gunyoung.tmb.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 운동 정보 열람할때 클라이언트측에서 보내는 날짜
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateDTO {
	private int year;
	private int month;
	private int date;
}
