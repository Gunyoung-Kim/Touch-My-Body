package com.gunyoung.tmb.utils.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.utils.DateUtil;

/**
* {@link DateUtil} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) DateUtil only
* @author kimgun-yeong
*
*/
public class DateUtilUnitTest {
	
	/*
	 * constructor
	 */
	
	@Test
	@DisplayName("생성자 호출시 예외 발생 확인")
	public void constructorTestThrowsAssertionError() throws NoSuchMethodException {
		//Given
		Constructor<DateUtil> constructor = DateUtil.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		
		//When, Then
		assertThrows(InvocationTargetException.class, () -> {
			constructor.newInstance();
		});
	}
	
	/*
	 * public static Calendar[] calendarForStartAndEndOfYearAndMonth(int year, int month)
	 */
	
	@Test
	@DisplayName("해당 연,월의 첫째날과 마지막 날을 반환 -> 2020년 1월")
	public void calendarForStartAndEndOfYearAndMonth202001() {
		//Given
		int year = 2020;
		int month = Calendar.JANUARY;
		
		//When
		Calendar[] result = DateUtil.calendarForStartAndEndOfYearAndMonth(year, month);
		
		//Then
		verify_for_calendarForStartAndEndOfYearAndMonth202001(result);
	}
	
	private void verify_for_calendarForStartAndEndOfYearAndMonth202001(Calendar[] result) {
		assertEquals(1, result[0].get(Calendar.DATE));
		assertEquals(31, result[1].get(Calendar.DATE));
	}
	
	@Test
	@DisplayName("해당 연,월의 첫째날과 마지막 날을 반환 -> 2020년 2월 (윤년)")
	public void calendarForStartAndEndOfYearAndMonth202002() {
		//Given
		int year = 2020;
		int month = Calendar.FEBRUARY;
		
		//When
		Calendar[] result = DateUtil.calendarForStartAndEndOfYearAndMonth(year, month);
		
		//Then
		verify_for_calendarForStartAndEndOfYearAndMonth202002(result);
	}
	
	private void verify_for_calendarForStartAndEndOfYearAndMonth202002(Calendar[] result) {
		assertEquals(1, result[0].get(Calendar.DATE));
		assertEquals(29, result[1].get(Calendar.DATE));
	}
	
	@Test
	@DisplayName("해당 연,월의 첫째날과 마지막 날을 반환 -> 2020년 4월")
	public void calendarForStartAndEndOfYearAndMonth202004() {
		//Given
		int year = 2020;
		int month = Calendar.APRIL;
		
		//When
		Calendar[] result = DateUtil.calendarForStartAndEndOfYearAndMonth(year, month);
		
		//Then
		verify_for_calendarForStartAndEndOfYearAndMonth202004(result);
	}
	
	private void verify_for_calendarForStartAndEndOfYearAndMonth202004(Calendar[] result) {
		assertEquals(1, result[0].get(Calendar.DATE));
		assertEquals(30, result[1].get(Calendar.DATE));
	}
	
	@Test
	@DisplayName("해당 연,월의 첫째날과 마지막 날을 반환 -> 2020년 12월")
	public void calendarForStartAndEndOfYearAndMonth202012() {
		//Given
		int year = 2020;
		int month = Calendar.DECEMBER;
		
		//When
		Calendar[] result = DateUtil.calendarForStartAndEndOfYearAndMonth(year, month);
		
		//Then
		verify_for_calendarForStartAndEndOfYearAndMonth202012(result);
	}
	
	private void verify_for_calendarForStartAndEndOfYearAndMonth202012(Calendar[] result) {
		assertEquals(1, result[0].get(Calendar.DATE));
		assertEquals(31, result[1].get(Calendar.DATE));
	}
}
