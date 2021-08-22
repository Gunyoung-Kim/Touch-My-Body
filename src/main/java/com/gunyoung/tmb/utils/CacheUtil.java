package com.gunyoung.tmb.utils;

/**
 * Cache 관련 상수 값들 포함
 * @author kimgun-yeong
 *
 */
public class CacheUtil {
	
	private CacheUtil() {};
	
	/*
	 *  ------------- CacheNames ----------------------------------
	 */
	
	public static final String USER_NAME = "user";
	public static final String MUSCLE_SORT_NAME = "muscle_sort";
	public static final String EXERCISE_SORT_NAME = "exercise_sort";
	public static final String COMMENT_LIKE_NAME = "commentLike";
	public static final String POST_LIKE_NAME = "postLike";
	
	/*
	 * ------------- Cache Expire Times By Minute  ----------------------------------
	 */
	
	public static final long CACHE_DEFAULT_EXPIRE_MIN = 1L;
	public static final long USER_EXPIRE_MIN = 2L;
	public static final long MUSCLE_SORT_EXPIRE_MIN = 2L;
	public static final long EXERCISE_SORT_EXPIRE_MIN = 1L;
	public static final long COMMENT_LIKE_EXPIRE_MIN = 1L;
	public static final long POST_LIKE_EXPIRE_MIN = 1L;
}
