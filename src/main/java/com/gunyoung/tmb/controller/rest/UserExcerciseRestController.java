package com.gunyoung.tmb.controller.rest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.tmb.aop.annotations.LoginIdSessionNotNull;
import com.gunyoung.tmb.domain.user.UserExercise;
import com.gunyoung.tmb.dto.reqeust.DateDTO;
import com.gunyoung.tmb.dto.response.UserExerciseIsDoneDTO;
import com.gunyoung.tmb.dto.response.UserExerciseWithDateDTO;
import com.gunyoung.tmb.services.domain.user.UserExerciseService;
import com.gunyoung.tmb.utils.SessionUtil;

import lombok.RequiredArgsConstructor;

/**
 * UserExercise 관련 요청 처리하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class UserExcerciseRestController {
	
	private final HttpSession session;
	
	private final UserExerciseService userExerciseService;
	
	/**
	 * 접속자의 특정 날짜의 운동 기록들 반환하는 메소드
	 * @param date year,month,date 포함하는 dto
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/records",method=RequestMethod.GET)
	@LoginIdSessionNotNull
	public List<UserExerciseWithDateDTO> getExerciseRecords(@ModelAttribute DateDTO date) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		Calendar paramDate = new GregorianCalendar(date.getYear(),date.getMonth(),date.getDate());
		
		List<UserExercise> userExerciseList = userExerciseService.findByUserIdAndDate(loginUserId, paramDate);
		
		return UserExerciseWithDateDTO.of(userExerciseList);
	}
	
	/**
	 * 접속자의 특정 달에 각 일에 운동 했는지 여부 반환하는 메소드 
	 * @param year 검색하려는 년도
	 * @param month 검색하려는 월
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/user/exercise/calendar/isdone",method=RequestMethod.GET)
	@LoginIdSessionNotNull
	public List<UserExerciseIsDoneDTO> getIsDoneList(@RequestParam("year") int year, @RequestParam("month") int month) {
		Long loginUserId = SessionUtil.getLoginUserId(session);
		
		List<UserExerciseIsDoneDTO> isDoneList = userExerciseService.findIsDoneDTOByUserIdAndYearAndMonth(loginUserId, year, month);
		
		return isDoneList;
	}
}
