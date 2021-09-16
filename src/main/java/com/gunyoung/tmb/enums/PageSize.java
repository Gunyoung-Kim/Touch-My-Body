package com.gunyoung.tmb.enums;

import lombok.Getter;

/**
 * 리스트를 포함하는 View의 리스트를 이루는 row 갯수
 * @author kimgun-yeong
 *
 */
@Getter
public enum PageSize {
	
	BY_NICKNAME_NAME_PAGE_SIZE(20),
	COMMUNITY_PAGE_SIZE(30),
	EXERCISE_INFO_TABLE_PAGE_SIZE(20),
	COMMENT_FOR_MANAGE_PAGE_SIZE(30),
	POST_FOR_MANAGE_PAGE_SIZE(30),
	MUSCLE_FOR_MANAGE_PAGE_SIZE(30),
	FEEDBACK_FOR_MANAGE_PAGE_SIZE(20),
	COMMENT_FOR_PROFILE_PAGE_SIZE(30),
	POST_FOR_PROFILE_PAGE_SIZE(30);

	private int size;

	private PageSize(int size) {
		this.size = size;
	}	
}
