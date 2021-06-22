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
	 */
	@Query("SELECT ue FROM UserExercise ue "
			+ "INNER JOIN ue.user u "
			+ "WHERE (u.id = :userId) "
			+ "and (ue.date = :date) ")
	public List<UserExercise> findUserExercisesByUserIdAndDate(@Param("userId")Long userId,@Param("date") Calendar date);
}
