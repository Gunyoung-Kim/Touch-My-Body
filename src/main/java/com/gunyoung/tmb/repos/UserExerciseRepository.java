package com.gunyoung.tmb.repos;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.user.UserExercise;

public interface UserExerciseRepository extends JpaRepository<UserExercise,Long>{
	
	/**
	 * User ID, UserExercise date 를 만족하는 UserExercise들 찾기 
	 * @param userId 찾으려는 UserExercise의 User ID
	 * @param date 찾으려는 UserExercise의 date
	 * @author kimgun-yeong
	 */
	@Query("SELECT ue FROM UserExercise ue "
			+ "INNER JOIN ue.user u "
			+ "WHERE (u.id = :userId) "
			+ "and (ue.date = :date) ")
	public List<UserExercise> findUserExercisesByUserIdAndDate(@Param("userId") Long userId, @Param("date") Calendar date);
	
	/**
	 * 특정 유저의 특정 날짜 사이에 존재하는 운동정보의 날짜들을 가져오는 쿼리 
	 * @param start 검색 시작 날짜
	 * @param end 검색 종료 날짜
	 * @author kimgun-yeong
	 */
	@Query("SELECT ue.date FROM UserExercise ue "
			+ "INNER JOIN ue.user u "
			+ "WHERE (u.id = :userId) "
			+ "AND (ue.date >= :startDay) "
			+ "AND (ue.date <= :endDay)")
	public List<Calendar> findUserExercisesIdForDayToDay(@Param("userId") Long userId, @Param("startDay") Calendar start, @Param("endDay") Calendar end);
	
	/**
	 * User Id로 만족하는 UserUser들 일괄 삭제
	 * @param user 삭제하려는 UserExercise들의 User ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically= true, flushAutomatically = true)
	@Query("DELETE FROM UserExercise ue "
			+ "WHERE ue.user.id = :userId")
	public void deleteAllByUserIdInQuery(@Param("userId") Long userId);
	
	/**
	 * Exercise Id로 만족하는 UserExercise들 일괄 삭제
	 * @param exerciseId 삭제하려는 UserExercise들의 Exercise ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM UserExercise ue "
			+ "WHERE ue.exercise.id = :exerciseId")
	public void deleteAllByExerciseIdInQuery(@Param("exerciseId") Long exerciseId);
}
