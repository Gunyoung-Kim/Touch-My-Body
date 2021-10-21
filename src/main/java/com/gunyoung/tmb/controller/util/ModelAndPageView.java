package com.gunyoung.tmb.controller.util;

import org.springframework.web.servlet.ModelAndView;

/**
 * Page View 를 반환하는 ModelAndView 상속 클래스
 * @author kimgun-yeong
 *
 */
public class ModelAndPageView extends ModelAndView {
	
	public static final int INDEX_NUM_PER_PAGE = 5;
	
	public static final String CURRENT_PAGE = "currentPage";
	public static final String START_INDEX = "startIndex";
	public static final String LAST_INDEX = "lastIndex";
	
	/**
	 * Page View 에서 Page의 index 들 설정하는 메소드
	 * @param currentPage 현재 페이지 넘버
	 * @param totalPageNum 총 페이지 갯수
	 * @author kimgun-yeong
	 */
	public void setPageNumbers(Integer currentPage, Long totalPageNum) {
		this.addObject(CURRENT_PAGE, currentPage);
		this.addObject(START_INDEX, (currentPage/INDEX_NUM_PER_PAGE)* INDEX_NUM_PER_PAGE + 1);
		this.addObject(LAST_INDEX,(currentPage/INDEX_NUM_PER_PAGE)* INDEX_NUM_PER_PAGE +INDEX_NUM_PER_PAGE > totalPageNum ? totalPageNum : (currentPage/INDEX_NUM_PER_PAGE)* INDEX_NUM_PER_PAGE +INDEX_NUM_PER_PAGE);
	}
}
