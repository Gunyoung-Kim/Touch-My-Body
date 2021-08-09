package com.gunyoung.tmb.controller.util;

import org.springframework.web.servlet.ModelAndView;

/**
 * Page View 를 반환하는 ModelAndView 상속 클래스
 * @author kimgun-yeong
 *
 */
public class ModelAndPageView extends ModelAndView {

	private static String CURRENT_PAGE = "currentPage";
	private static String START_INDEX = "startIndex";
	private static String LAST_INDEX = "lastIndex";
	
	/**
	 * Page View 에서 Page의 index 들 설정하는 메소드
	 * @param currentPage 현재 페이지 넘버
	 * @param pageSize 한 페이지에 포함하는 콘텐츠 라인 갯수
	 * @param totalPageNum 총 페이지 갯수
	 * @author kimgun-yeong
	 */
	public void setPageNumbers(Integer currentPage, Integer pageSize, Long totalPageNum) {
		this.addObject(CURRENT_PAGE, currentPage);
		this.addObject(START_INDEX, (currentPage/pageSize)*pageSize+1);
		this.addObject(LAST_INDEX,(currentPage/pageSize)*pageSize+pageSize-1 > totalPageNum ? totalPageNum : (currentPage/pageSize)*pageSize+pageSize-1);
	}
}
