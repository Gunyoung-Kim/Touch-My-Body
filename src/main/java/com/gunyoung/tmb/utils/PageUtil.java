package com.gunyoung.tmb.utils;

public class PageUtil {
	
	/**
	 * 인스턴스화 방지
	 * @author kimgun-yeong
	 */
	private PageUtil() {
		throw new AssertionError();
	}
	
	public static final int BY_NICKNAME_NAME_PAGE_SIZE = 20;
	
	public static final int COMMUNITY_PAGE_SIZE = 30;
	
	public static final int EXERCISE_INFO_TABLE_PAGE_SIZE = 20;
	
	public static final int COMMENT_FOR_MANAGE_PAGE_SIZE = 30;
	public static final int POST_FOR_MANAGE_PAGE_SIZE = 30;
	public static final int MUSCLE_FOR_MANAGE_PAGE_SIZE = 30;
	public static final int FEEDBACK_FOR_MANAGE_PAGE_SIZE = 20;
	
	public static final int COMMENT_FOR_PROFILE_PAGE_SIZE = 30;
	public static final int POST_FOR_PROFILE_PAGE_SIZE = 30;

}
