package com.gunyoung.tmb.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 날짜 관련 유틸리티 클래스, 주로 날짜의 변환 기능
 * @author kimgun-yeong
 *
 */
public class DateUtil {
	
	/**
	 * 인스턴스화 방지
	 * @author kimgun-yeong
	 */
	private DateUtil() {
		throw new AssertionError();
	}
	
	/**
	 * 해당 연,월의 첫째날과 마지막 날을 반환하는 메소드
	 * @param year
	 * @param month
	 * @return
	 * @author kimgun-yeong
	 */
	public static Calendar[] calendarForStartAndEndOfYearAndMonth(int year, int month) {
		int startDate = 1;
		int endDate = 0;
		Calendar start = new GregorianCalendar(year,month,startDate);
		endDate = start.getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar end = new GregorianCalendar(year,month,endDate);
		
		return new Calendar[] {start,end};
	}
}
