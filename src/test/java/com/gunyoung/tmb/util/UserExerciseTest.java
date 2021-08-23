package com.gunyoung.tmb.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.SaveUserExerciseDTO;

/**
 * Test 클래스 전용 UserExercise 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class UserExerciseTest {
	
	public static final Calendar DEFAULT_CALENDAR = new GregorianCalendar(1999,Calendar.JANUARY,16);
	
	/**
	 * 테스트용 UserExercise 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static UserExercise getUserExerciseInstance() {
		return getUserExerciseInstance(DEFAULT_CALENDAR);
	}
	
	/**
	 * 테스트용 UserExercise 인스턴스 반환 <br>
	 * date 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static UserExercise getUserExerciseInstance(Calendar date) {
		UserExercise ue = UserExercise.builder()
				.date(date)
				.description("description")
				.laps(1)
				.sets(1)
				.weight(1)
				.build();
		return ue;
	}
	
	/**
	 * Repository를 통해 존재하지 않는 UserExercise ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistUserExerciseId(JpaRepository<UserExercise, Long> userExerciseRepository) {
		Long nonExistUserExerciseId = Long.valueOf(1);
		
		for(UserExercise u : userExerciseRepository.findAll()) {
			nonExistUserExerciseId = Math.max(nonExistUserExerciseId, u.getId());
		}
		nonExistUserExerciseId++;
		
		return nonExistUserExerciseId;
	}
	
	/**
	 * Repository를 사용해 DB에 인자로 전해진 num 만큼 UserExercise 생성 후 저장 <br>
	 * 모든 UserExercise 이름 다르게 저장 
	 * @author kimgun-yeong
	 */
	public static List<UserExercise> addNewUserExercisesInDBByNum(int num, JpaRepository<UserExercise, Long> userExerciseRepository) {
		List<UserExercise> newUserExercises = new ArrayList<>();
		for(int i=0;i < num;i++) {
			UserExercise newUserExercise = getUserExerciseInstance(DEFAULT_CALENDAR);
			newUserExercises.add(newUserExercise);
		}
		return userExerciseRepository.saveAll(newUserExercises);
	}

	/**
	 * 테스트 용 {@link com.gunyoung.tmb.dto.reqeust.SaveUserExerciseDTO} 인스턴스 반환 <br>
	 * exerciseName 커스터마이징 가능 
	 * @author kimgun-yeong
	 */
	public static SaveUserExerciseDTO getSaveUserExerciseDTOInstance(String exerciseName) {
		SaveUserExerciseDTO saveUserExerciseDTO = SaveUserExerciseDTO.builder()
				.laps(1)
				.sets(1)
				.weight(1)
				.description("description")
				.date(new GregorianCalendar(1999, Calendar.JANUARY, 16))
				.exerciseName(exerciseName)
				.build();
		
		return saveUserExerciseDTO;
	}
	
	/**
	 * {@link com.gunyoung.tmb.dto.reqeust.SaveUserExerciseDTO} 객체에 바인딩 될 수 있는 MultiValueMap 반환 <br>
	 * exerciseName 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static MultiValueMap<String, String> getSaveUserExerciseDTOMap(String exerciseName) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("laps", "1");
		map.add("sets", "1");
		map.add("weight", "1");
		map.add("description", "des");
		map.add("date", "2021-05-05");
		map.add("exerciseName", exerciseName);
		return map;
	}
}
