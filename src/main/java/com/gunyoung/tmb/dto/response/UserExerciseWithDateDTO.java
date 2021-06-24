package com.gunyoung.tmb.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.gunyoung.tmb.domain.user.UserExercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
