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
	 * 해당 연,월의 첫째날과 마지막 날을 반환하는 메소드 
	 * @param year
	 * @param month
	 * @return
	 * @author kimgun-yeong
	 */
	public static Calendar[] calendarForStartAndEndOfYearAndMonth(int year, int month) {
		Calendar start = new GregorianCalendar(year,month,1);
		Calendar end = new GregorianCalendar(year,month,start.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println(start.get(Calendar.YEAR) +"." + start.get(Calendar.MONTH) +"." + start.get(Calendar.DATE));
		System.out.println(end.get(Calendar.YEAR) +"." + end.get(Calendar.MONTH) +"." + end.get(Calendar.DATE));
		return new Calendar[] {start,end};
	}
}
