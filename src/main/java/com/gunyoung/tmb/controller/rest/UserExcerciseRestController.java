package com.gunyoung.tmb.controller.rest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.DateDTO;
import com.gunyoung.tmb.dto.response.UserExerciseWithDateDTO;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserExcerciseRestController {
	
	private final HttpSession session;
	
	private final UserExerciseService userExerciseService;
	
	/**
	 * 
	 * @param date year,month,date 포함하는 dto
	 * @return
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/records",method=RequestMethod.GET)
	public List<UserExerciseWithDateDTO> getExerciseRecords(@ModelAttribute DateDTO date) {
		Long userId = SessionUtil.getLoginUserId(session);
		
		Calendar paramDate = new GregorianCalendar(date.getYear(),date.getMonth(),date.getDate());
		
		List<UserExercise> userExerciseList = userExerciseService.findByUserIdAndDate(userId, paramDate);
		
		return UserExerciseWithDateDTO.of(userExerciseList);
	}
}
