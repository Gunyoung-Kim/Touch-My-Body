package com.gunyoung.tmb.services.domain.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;
import com.gunyoung.tmb.precondition.Preconditions;
import com.gunyoung.tmb.repos.UserExerciseRepository;
import com.gunyoung.tmb.utils.DateUtil;

import lombok.RequiredArgsConstructor;

/**
 * UserExerciseService 구현 클래스
 * @author kimgun-yeong
 *
 */
@Service("userExerciseService")
@Transactional
@RequiredArgsConstructor
public class UserExerciseServiceImpl implements UserExerciseService {

	private final UserExerciseRepository userExerciseRepository;
	
	@Override
	@Transactional(readOnly=true)
	public UserExercise findById(Long id) {
		Optional<UserExercise> result = userExerciseRepository.findById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserExercise> findByUserIdAndDate(Long userId, Calendar date) {
		return userExerciseRepository.findUserExercisesByUserIdAndDate(userId, date);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<UserExerciseIsDoneDTO> findIsDoneDTOByUserIdAndYearAndMonth(Long userId, int year, int month) {
		Preconditions.notLessThanInt(year, 0, "Given year should be not less than zero");
		Preconditions.notLessThanInt(month, Calendar.JANUARY, "Given month should be not less than Calendar.JANUARY");
		Preconditions.notMoreThanInt(month, Calendar.DECEMBER, "Given month should be not less than Calendar.DECEMBER");
		
		Calendar[] firstAndLastDay = DateUtil.calendarForStartAndEndOfYearAndMonth(year, month);
		boolean[] isDoneArr = getIsDoneArrayFromRepository(userId, firstAndLastDay);
		
		int lastDateOfMonth = firstAndLastDay[1].get(Calendar.DATE);
		return getUserExerciseIsDoneDTOList(lastDateOfMonth, isDoneArr);
	}
	
	private boolean[] getIsDoneArrayFromRepository(Long userId, Calendar[] firstAndLastDay) {
		Calendar firstDayOfMonth = firstAndLastDay[0];
		Calendar lastDayOfMonth = firstAndLastDay[1];
		List<Calendar> calendarList = userExerciseRepository.findUserExercisesIdForDayToDay(userId, firstDayOfMonth, lastDayOfMonth);
		boolean[] isDoneArr = new  boolean[lastDayOfMonth.get(Calendar.DATE)+1];
		for(Calendar c : calendarList) {
			isDoneArr[c.get(Calendar.DATE)] = true;
		}
		return isDoneArr;
	}
	
	private List<UserExerciseIsDoneDTO> getUserExerciseIsDoneDTOList(int lastDateOfMonth, boolean[] isDoneArr) {
		List<UserExerciseIsDoneDTO> dtoList = new ArrayList<>();
		for(int i=1;i<=lastDateOfMonth;i++) {
			UserExerciseIsDoneDTO dto = UserExerciseIsDoneDTO.builder()
					.date(i)
					.isDone(isDoneArr[i])
					.build();
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	@Override
	public UserExercise save(UserExercise userExercise) {
		return userExerciseRepository.save(userExercise);
	}

	@Override
	public void delete(UserExercise userExercise) {
		userExerciseRepository.delete(userExercise);
	}
	
	@Override
	public void deleteAllByUserId(Long userId) {
		userExerciseRepository.deleteAllByUserIdInQuery(userId);
	}

	@Override
	public void deleteAllByExerciseId(Long exerciseId) {
		userExerciseRepository.deleteAllByExerciseIdInQuery(exerciseId);
	}
}
