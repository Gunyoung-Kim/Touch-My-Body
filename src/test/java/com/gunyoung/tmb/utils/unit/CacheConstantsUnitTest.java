package com.gunyoung.tmb.utils.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.utils.CacheConstants;

/**
* {@link CacheConstants} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) CacheUtil only
* @author kimgun-yeong
*
*/
public class CacheConstantsUnitTest {
	
	/*
	 * constructor
	 */
	
	@Test
	@DisplayName("생성자 호출시 예외 발생 확인")
	public void constructorTestThrowsAssertionError() throws NoSuchMethodException {
		//Given
		Constructor<CacheConstants> constructor = CacheConstants.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		
		//When, Then
		assertThrows(InvocationTargetException.class, () -> {
			constructor.newInstance();
		});
	}
	
}
