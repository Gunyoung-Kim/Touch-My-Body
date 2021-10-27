package com.gunyoung.tmb.precondition.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.precondition.PreconditionViolationException;
import com.gunyoung.tmb.precondition.Preconditions;

class PreconditionsUnitTest {
	
	/*
	 * private Preconditions()
	 */
	
	@Test
	@DisplayName("생성자 호출 -> 에러 발생")
	void constructorTest() throws NoSuchMethodException{
		//Given
		Constructor<Preconditions> constructor = Preconditions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		
		//When, Then
		assertThrows(InvocationTargetException.class, () -> {
			constructor.newInstance();
		});
	}
	
	/*
	 * public static <T> T notNull(T object, String message)
	 */
	
	@Test
	@DisplayName("Object null 확인 -> null임")
	void notNullTestNull() {
		//Given
		String messageForException = "Should be nonNull";
		
		//When
		assertThrows(PreconditionViolationException.class, () -> {
			Preconditions.notNull(null, messageForException);
		});
	}
}
