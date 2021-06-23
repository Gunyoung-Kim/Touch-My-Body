package com.gunyoung.tmb.controller.rest;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.DateDTO;
import com.gunyoung.tmb.services.domain.exercise.ExerciseService;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
import com.gunyoung.tmb.utils.SessionUtil;

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
	public List<UserExercise> getExerciseRecords(@ModelAttribute("date")DateDTO date) {
		Long userId = SessionUtil.getLoginUserId(session);
		Calendar paramDate = Calendar.getInstance();
		paramDate.set(date.getYear(), date.getMonth(), date.getDate());
		
		return userExerciseService.findByUserIdAndDate(userId, paramDate);
	}
	
	/**
	 * 각 부위별 운동 종류 반환하는 메소드
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/addrecord/getexercises",method=RequestMethod.GET)
	public Map<String, List<String>> getExercisesByNameAndTarget() {
		return exerciseService.getAllExercisesNamewithSorting();
	}
}
