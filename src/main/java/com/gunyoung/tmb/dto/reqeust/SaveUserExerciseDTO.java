package com.gunyoung.tmb.dto.reqeust;

import java.util.Calendar;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.gunyoung.tmb.domain.user.UserExercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에서 유저의 운동기록 추가를 위해 데이터 보낼때 사용
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserExerciseDTO {
	
	@Max(300)
	@Min(1)
	private Integer laps;
	
	@Max(100)
	@Min(1)
	private Integer sets;
	
	@Max(10000)
	@Min(1)
	private Integer weight;
	
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar date;
	
	private String exerciseName;
	
	/**
	 * SaveUserExerciseDTO에 담긴 정보를 통해 UserExercise 생성 후 반환
	 * @author kimgun-yeong
	 */
	public static UserExercise toUserExercise(SaveUserExerciseDTO dto) {
		UserExercise userExercise = UserExercise.builder()
				.laps(dto.getLaps())
				.sets(dto.getSets())
				.weight(dto.getWeight())
				.date(dto.getDate())
				.description(dto.getDescription())
				.build();
		
		return userExercise;
	}
}
