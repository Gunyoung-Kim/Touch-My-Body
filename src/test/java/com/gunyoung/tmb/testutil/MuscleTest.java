package com.gunyoung.tmb.testutil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO;
import com.gunyoung.tmb.enums.TargetType;

/**
 * Test 클래스 전용 Muscle 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class MuscleTest {
	
	public final static String DEFAULT_NAME = "muscle";
	
	/**
	 * 테스트용 Muscle 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Muscle getMuscleInstance() {
		return getMuscleInstance(DEFAULT_NAME, TargetType.ARM);
	}
	
	/**
	 * 테스트용 Muscle 인스턴스 반환 <br>
	 * name 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Muscle getMuscleInstance(String name) {
		return getMuscleInstance(name, TargetType.ARM);
	}
	
	/**
	 * 테스트용 Muscle 인스턴스 반환 <br>
	 * name, type 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Muscle getMuscleInstance(String name, TargetType type) {
		Muscle muscle = Muscle.builder()
				.category(type)
				.name(name)
				.build();
		
		return muscle;
	}
	
	/**
	 * 테스트용 SaveMuscleDTO 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static SaveMuscleDTO getSaveMuscleDTOInstance() {
		return getSaveMuscleDTOInstance("name", TargetType.ARM.getKoreanName());
	}
	
	/**
	 * 테스트용 SaveMuscleDTO 인스턴스 반환 <br>
	 * name, categoy 커스터마이징 가능
	 * @author kimgun-yeongs
	 */
	public static SaveMuscleDTO getSaveMuscleDTOInstance(String name, String category) {
		SaveMuscleDTO dto = SaveMuscleDTO.builder()
				.name(name)
				.category(category)
				.build();
		
		return dto;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 Muscle ID 반환 <br>
	 * 모든 Muscle의 이름 다르게 저장
	 * @author kimgun-yeong
	 */
	public static Long getNonExistMuscleId(JpaRepository<Muscle, Long> muscleRepository) {
		Long nonExistMuscleId = Long.valueOf(1);
		
		for(Muscle muscle : muscleRepository.findAll()) {
			nonExistMuscleId = Math.max(nonExistMuscleId, muscle.getId());
		}
		
		nonExistMuscleId++;
		return nonExistMuscleId;
	}
	
	/**
	 * Repository를 사용해 DB에 인자로 전해진 num 만큼 Muscle 생성 후 저장
	 * @author kimgun-yeong
	 */
	public static List<Muscle> addNewMusclesInDBByNum(int num, JpaRepository<Muscle, Long> muscleRepository) {
		List<Muscle> newMuscles = new ArrayList<>();
		for(int i=0;i < num;i++) {
			Muscle newMuscle = getMuscleInstance(DEFAULT_NAME + i);
			newMuscles.add(newMuscle);
		}
		return muscleRepository.saveAll(newMuscles);
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO} 에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * name, category value 커스터마이징 가능 <br>
	 * category는 {@link TargetType} koreanName으로 입력
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getSaveMuscleDTOMap(String name, String category) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("name", name);
		map.add("category", category);
		return map;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveMuscleDTO} 에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * name, category value 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String,String> getSaveMuscleDTOMap(String name, TargetType type) {
		MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
		map.add("name", name);
		map.add("category", type.getKoreanName());
		return map;
	}
}
