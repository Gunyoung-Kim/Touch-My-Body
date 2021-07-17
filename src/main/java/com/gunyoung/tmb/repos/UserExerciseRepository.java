package com.gunyoung.tmb.repos;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.tmb.domain.user.UserExercise;

public interface UserExerciseRepository extends JpaRepository<UserExercise,Long>{
	
	/**
	 * inner join을 통한 성능 향상
	 * @param userId
	 * @param date
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ue FROM UserExercise ue "
			+ "INNER JOIN ue.user u "
			+ "WHERE (u.id = :userId) "
			+ "and (ue.date = :date) ")
	public List<UserExercise> findUserExercisesByUserIdAndDate(@Param("userId")Long userId,@Param("date") Calendar date);
	
	/**
	 * 특정 유저의 특정 날짜 사이에 존재하는 운동정보의 날짜들을 가져오는 쿼리 
	 * @param start
	 * @param end
	 * @return
	 * @author kimgun-yeong
	 */
	@Query("SELECT ue.date FROM UserExercise ue "
			+ "INNER JOIN ue.user u "
			+ "WHERE (u.id = :userId) "
			+ "AND (ue.date >= :startDay) "
			+ "AND (ue.date <= :endDay)")
	public List<Calendar> findUserExercisesIdForDayToDay(@Param("userId")Long userId, @Param("startDay")Calendar start, @Param("endDay")Calendar end);
}
