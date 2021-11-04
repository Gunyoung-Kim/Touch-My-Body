package com.gunyoung.tmb.precondition.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
	void constructorTest() throws NoSuchMethodException {
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
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			Preconditions.notNull(null, messageForException);
		});
	}
	
	@Test
	@DisplayName("Object null 확인 -> notnull") 
	void notNullTest() {
		//Given
		Long object = Long.valueOf(28);
		String messageForException = "Should be nonNull";
		
		//When
		Long result = Preconditions.notNull(object, messageForException);
		
		//Then
		assertEquals(object, result);
	}
	
	/*
	 * public static int notLessThanInt(int object, int another, String message)
	 */
	
	@Test
	@DisplayName("int object가 another 이상인지 확인 -> 이상 아님")
	void notLessThanIntTestNotLessThan() {
		//Given
		int object = 36;
		int another = 88;
		String messageForException = "object is not greater than antoher";
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			Preconditions.notLessThanInt(object, another, messageForException);
		});
	}
	
	@Test
	@DisplayName("int object가 another 이상인지 확인 -> 이상임")
	void notLessThanIntTest() {
		//Given
		int object = 92;
		int another = 24;
		String messageForException = "object is not greater than antoher";
		
		//When
		int result = Preconditions.notLessThanInt(object, another, messageForException);
		
		//Then
		assertEquals(object, result);
	}
	
	/*
	 * public static int notMoreThanInt(int object, int another, String message)
	 */
	
	@Test
	@DisplayName("int object가 another 이하인지 확인 -> 이하 아님")
	void notMoreThanIntTestNotLessThan() {
		//Given
		int object = 225;
		int another = 28;
		String messageForException = "object is not less than antoher";
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			Preconditions.notMoreThanInt(object, another, messageForException);
		});
	}
	
	@Test
	@DisplayName("int object가 another 이하인지 확인 -> 이하임")
	void notMoreThanIntTest() {
		//Given
		int object = 22;
		int another = 29;
		String messageForException = "object is not less than antoher";
		
		//When
		int result = Preconditions.notMoreThanInt(object, another, messageForException);
		
		//Then
		assertEquals(object, result);
	}
	
	/*
	 * public static <T extends Comparable<T>> T notLessThan(T object, T another, String message)
	 */
	
	@Test
	@DisplayName("object가 another 이상인지 확인 -> 크지 않음")
	void notLessThanTestNotGreater() {
		//Given
		Integer object = Integer.valueOf(24);
		Integer another = Integer.valueOf(100);
		String messageForException = "object is not greater than antoher";
		
		//When, Then
		assertThrows(PreconditionViolationException.class, () -> {
			Preconditions.notLessThan(object, another, messageForException);
		});
	}
	
	@Test
	@DisplayName("object가 another 이상인지 확인 -> 큼")
	void notLessThanTestGreater() {
		//Given
		Integer object = Integer.valueOf(82);
		Integer another = Integer.valueOf(18);
		String messageForException = "object is not greater than antoher";
		
		//When
		Integer result = Preconditions.notLessThan(object, another, messageForException);
		
		//Then
		assertEquals(object, result);
	}
	
	/*
	 * public static <T extends Comparable<T>> T notMoreThan(T object, T another, String message)
	 */
	
	@Test
	@DisplayName("object가 another보다 작은지 확인 -> 작지 않음")
	void notMoreThanTestNotLess() {
		//Given
		Integer object = Integer.valueOf(72);
		Integer another = Integer.valueOf(66);
		String messageForException = "object is not less than antoher";
		
		//When, Then 
		assertThrows(PreconditionViolationException.class, () -> {
			Preconditions.notMoreThan(object, another, messageForException);
		});
	}
	
	@Test
	@DisplayName("object가 another보다 작은지 확인 -> 작음")
	void notMoreThanTestLess() {
		//Given
		Integer object = Integer.valueOf(10);
		Integer another = Integer.valueOf(30);
		String messageForException = "object is not less than antoher";
		
		//When
		Integer result = Preconditions.notMoreThan(object, another, messageForException);
		
		//Then
		assertEquals(object, result);
	}
}
