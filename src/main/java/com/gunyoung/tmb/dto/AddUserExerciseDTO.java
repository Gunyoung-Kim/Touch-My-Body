package com.gunyoung.tmb.dto;

import java.util.Calendar;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.gunyoung.tmb.domain.user.UserExercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserExerciseDTO {
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
	
	private Calendar date;
	
	private String exerciseName;
	
	public static UserExercise toUserExercise(AddUserExerciseDTO dto) {
		
		
		
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
