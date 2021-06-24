package com.gunyoung.tmb.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.gunyoung.tmb.domain.user.UserExercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에게 해당 날짜의 운동 기록 반환할때 사용
 * @author kimgun-yeong
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExerciseWithDateDTO {
	private Integer laps;
	
	private Integer sets;
	
	private Integer weight;
	
	private String description;
	
	private String exerciseName;
	
	/**
	 * UserExercise List 객체를 UserExerciseWithDateDTO 로 변환하는 메소드
	 * @param list
	 * @return
	 */
	public static List<UserExerciseWithDateDTO> of(List<UserExercise> list) {
		List<UserExerciseWithDateDTO> result = new ArrayList<>();
		for(UserExercise ue: list) {
			UserExerciseWithDateDTO dto = UserExerciseWithDateDTO.builder()
					.laps(ue.getLaps())
					.sets(ue.getSets())
					.weight(ue.getWeight())
					.description(ue.getDescription())
					.exerciseName(ue.getExercise().getName())
					.build();
			
			result.add(dto);
		}
		
		return result;
	}
	
	/**
	 * UserExercise 객체를 UserExerciseWithDateDTO로 변환하는 메소드
	 * @param userExercise
	 * @return
	 */
	public static UserExerciseWithDateDTO of(UserExercise userExercise) {
		UserExerciseWithDateDTO dto = UserExerciseWithDateDTO.builder()
				.laps(userExercise.getLaps())
				.sets(userExercise.getSets())
				.weight(userExercise.getWeight())
				.description(userExercise.getDescription())
				.exerciseName(userExercise.getExercise().getName())
				.build();
		
		return dto;
	}
}
