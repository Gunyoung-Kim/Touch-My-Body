package com.gunyoung.tmb.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.enums.TargetType;

/**
 * Test 클래스 전용 Muscle 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class MuscleTest {
	
	/**
	 * 테스트용 Muscle 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Muscle getMuscleInstance() {
		return getMuscleInstance("name", TargetType.ARM);
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
	 * Repository를 통해 존재하지 않는 Muscle ID 반환
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
