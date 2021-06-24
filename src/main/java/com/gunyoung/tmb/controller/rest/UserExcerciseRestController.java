package com.gunyoung.tmb.controller.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.DateDTO;
import com.gunyoung.tmb.dto.response.ExerciseInfoBySortDTO;
import com.gunyoung.tmb.dto.response.UserExerciseWithDateDTO;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;

@RestController
public class UserExcerciseRestController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserExerciseService userExerciseService;
	
	@Autowired
	ExerciseService exerciseService;
	
	/**
	 * 
	 * @param date year,month,date 포함하는 dto
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/records",method=RequestMethod.GET)
	public List<UserExerciseWithDateDTO> getExerciseRecords(@ModelAttribute DateDTO date) {
		//Long userId = SessionUtil.getLoginUserId(session);
		Long userId = Long.valueOf(1);
		
		Calendar paramDate = new GregorianCalendar(date.getYear(),date.getMonth(),date.getDate());
		
		System.out.println(paramDate.get(Calendar.YEAR)+"-" + paramDate.get(Calendar.MONTH) +"- " + paramDate.get(Calendar.DATE));
		
		List<UserExercise> userExerciseList = userExerciseService.findByUserIdAndDate(userId, paramDate);
		
		return UserExerciseWithDateDTO.of(userExerciseList);
	}
	
	/**
	 * 각 부위별 운동 종류 반환하는 메소드
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/addrecord/getexercises",method=RequestMethod.GET)
	public List<ExerciseInfoBySortDTO> getExercisesByNameAndTarget() {
		List<ExerciseInfoBySortDTO> resultList = new ArrayList<>();
		Map<String ,List<String>> map = exerciseService.getAllExercisesNamewithSorting();
		
		Set<String> keySet = map.keySet();
		
		for(String key: keySet) {
			resultList.add(new ExerciseInfoBySortDTO(key,map.get(key)));
		}
		
		return resultList;
	}
}
