package com.gunyoung.tmb.controller.util.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.controller.util.ModelAndPageView;

/**
 * {@link ModelAndPageView} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ModelAndPageView only
 * @author kimgun-yeong
 *
 */
class ModelAndPageViewUnitTest {
	
	/*
	 * void setPageNumbers(Integer currentPage, Integer pageSize, Long totalPageNum)
	 */
	
	@Test
	@DisplayName("페이지 넘버 세팅 -> 현재: 1, 페이지당 인덱스 개수: 5, 총 페이지 개수: 7 ( 1 2 3 4 5), 시작 인덱스 확인")
	void setPageNumbersTestStartToLastSameToSizeCheckStartIndex() {
		//Given
		int currentPage = 1;
		long totalPage = 7;
		
		ModelAndPageView modelAndPageView = new ModelAndPageView();
		
		//When
		modelAndPageView.setPageNumbers(currentPage, totalPage);
		
		//Then
		assertEquals(1 ,modelAndPageView.getModel().get(ModelAndPageView.START_INDEX));
	}
	
	@Test
	@DisplayName("페이지 넘버 세팅 -> 현재: 1, 페이지당 인덱스 개수: 5, 총 페이지 개수: 7 ( 1 2 3 4 5), 마지막 인덱스 확인")
	void setPageNumbersTestStartToLastSameToSizeCheckLastIndex() {
		//Given
		int currentPage = 1;
		long totalPage = 7;
		
		ModelAndPageView modelAndPageView = new ModelAndPageView();
		
		//When
		modelAndPageView.setPageNumbers(currentPage, totalPage);
		
		//Then
		assertEquals(Long.valueOf(5) ,modelAndPageView.getModel().get(ModelAndPageView.LAST_INDEX));
	}
	
	@Test
	@DisplayName("페이지 넘버 세팅 -> 현재: 6, 페이지당 인덱스 개수: 5, 총 페이지 개수: 7 (6 7), 시작 인덱스 확인")
	void setPageNumbersTestStartToLastLessThenSizeCheckStartIndex() {
		//Given
		int currentPage = 6;
		long totalPage = 7;
		
		ModelAndPageView modelAndPageView = new ModelAndPageView();
		
		//When
		modelAndPageView.setPageNumbers(currentPage, totalPage);
		
		//Then
		assertEquals(6 ,modelAndPageView.getModel().get(ModelAndPageView.START_INDEX));
	}
	
	@Test
	@DisplayName("페이지 넘버 세팅 -> 현재: 6, 페이지당 인덱스 개수: 5, 총 페이지 개수: 7 (6 7), 마지막 인덱스 확인")
	void setPageNumbersTestStartToLastLessThenSize() {
		//Given
		int currentPage = 6;
		long totalPage = 7;
		
		ModelAndPageView modelAndPageView = new ModelAndPageView();
		
		//When
		modelAndPageView.setPageNumbers(currentPage, totalPage);
		
		//Then
		assertEquals(Long.valueOf(7) ,modelAndPageView.getModel().get(ModelAndPageView.LAST_INDEX));
	}
}
