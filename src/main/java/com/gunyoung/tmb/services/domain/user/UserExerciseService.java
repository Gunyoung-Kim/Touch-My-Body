package com.gunyoung.tmb.services.domain.user;

import java.util.Calendar;
import java.util.List;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;

public interface UserExerciseService {
	
	/**
	 * ID로 UserExercise 찾기
	 * @param id 찾으려는 UserExercise의 id값
	 * @return UserExercise, null(해당 id의 UserExercise 없을때)
	 * @since 11
	 * @author kimgun-yeong
	 */
	public UserExercise findById(Long id);
	
	/**
	 * User Id, UserExercise date 로 UserExercise들 찾기
	 * @param userId 운동기록의 주인 Id
	 * @param date 찾으려는 날짜
	 * @return 조건을 만족하는 운동 기록들
	 * @author kimgun-yeong
	 */
	public List<UserExercise> findByUserIdAndDate(Long userId,Calendar date);
	
	/**
	 * 특정 유저의 특정 년,월에 각 일마다 운동했는지 여부 반환하는 메소드
	 * @param userId 운동기록의 주인 Id
	 * @param year 검색 년도
	 * @param month 검색 월
	 * @author kimgun-yeong
	 */
	public List<UserExerciseIsDoneDTO> findIsDoneDTOByUserIdAndYearAndMonth(Long userId, int year, int month);
	
	/**
	 * UserExercise 생성 및 수정
	 * @param userExercise 저장하려는 UserExercise
	 * @return 저장된 결과
	 * @author kimgun-yeong
	 */
	public UserExercise save(UserExercise userExercise);
	
	/**
	 * UserExercise 삭제
	 * @param userExercise 삭제하려는 UserExercise
	 * @author kimgun-yeong
	 */
	public void delete(UserExercise userExercise);
}
