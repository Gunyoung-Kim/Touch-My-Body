package com.gunyoung.tmb.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Exercise;
import com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO;
import com.gunyoung.tmb.enums.TargetType;

/**
 * Test 클래스 전용 Exercise 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class ExerciseTest {
	
	public static final String DEFAULT_NAME = "exercise";
	
	/**
	 * 테스트용 Exercise 인스턴스 반환 
	 * @author kimgun-yeong
	 */
	public static Exercise getExerciseInstance() {
		return getExerciseInstance(DEFAULT_NAME, TargetType.BACK);
	}
	
	/**
	 * 테스트용 Exercise 인스턴스 반환 <br>
	 * name 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Exercise getExerciseInstance(String name) {
		return getExerciseInstance(name, TargetType.BACK);
	}
	
	/**
	 * 테스트용 Exercise 인스턴스 반환 <br>
	 * name, target 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Exercise getExerciseInstance(String name, TargetType target) {
		Exercise exercise = Exercise.builder()
				.name(name)
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(target)
				.build();
		
		return exercise;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 Exercise ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistExerciseId(JpaRepository<Exercise, Long> exerciseRepository) {
		Long nonExistExerciseId = Long.valueOf(1);
		
		for(Exercise u : exerciseRepository.findAll()) {
			nonExistExerciseId = Math.max(nonExistExerciseId, u.getId());
		}
		nonExistExerciseId++;
		
		return nonExistExerciseId;
	}
	
	/**
	 * Repository를 사용해 DB에 인자로 전해진 num 만큼 Exercise 생성 후 저장 <br>
	 * 모든 Exercise 이름 다르게 저장 
	 * @author kimgun-yeong
	 */
	public static List<Exercise> addNewExercisesInDBByNum(int num, JpaRepository<Exercise, Long> exerciseRepository) {
		List<Exercise> newExercises = new ArrayList<>();
		for(int i=0;i < num;i++) {
			Exercise newExercise = getExerciseInstance(DEFAULT_NAME + i);
			newExercises.add(newExercise);
		}
		return exerciseRepository.saveAll(newExercises);
	}
	
	/**
	 * 테스트용 {@link com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO} 인스턴스 반환 <br>
	 * name 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static SaveExerciseDTO getSaveExerciseDTOInstance(String name) {
		SaveExerciseDTO dto = SaveExerciseDTO.builder()
				.name(name)
				.description("description")
				.caution("caution")
				.movement("movement")
				.target(TargetType.BACK.getKoreanName())
				.build();
		
		return dto;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveExerciseDTO} 에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * name value 커스터마이징 가능
	 * @param name
	 * @return
	 */
	public static MultiValueMap<String,String> getSaveExerciseDTOMap(String name) {
		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
		map.add("name", name);
		map.add("description", "d");
		map.add("caution", "c");
		map.add("movement", "m");
		map.add("target", "가슴");
		
		return map;
	}
}
