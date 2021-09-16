package com.gunyoung.tmb.utils.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.tmb.utils.HttpRequestUtil;

/**
* {@link HttpRequestUtil} 에 대한 테스트 클래스 <br>
* 테스트 범위: (단위 테스트) HttpRequestUtil only
* @author kimgun-yeong
*
*/
public class HttpRequestUtilUnitTest {
	
	/*
	 * constructor
	 */
	
	@Test
	@DisplayName("생성자 호출시 예외 발생 확인")
	public void constructorTestThrowsAssertionError() throws NoSuchMethodException {
		//Given
		Constructor<HttpRequestUtil> constructor = HttpRequestUtil.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		
		//When, Then
		assertThrows(InvocationTargetException.class, () -> {
			constructor.newInstance();
		});
	}
	
	/*
	 *  public static String getRemoteHost(HttpServletRequest request)
	 */
	
	@Test
	@DisplayName("HttpRequest의 RemoteHost를 반환 -> 'X-Real-IP' 헤더 있음")
	public void getRemoteHostTestWithX_REAL_IP() {
		//Given
		String remoteHostFromX_REAL_IP = "66.72.22.1";
		HttpServletRequest request = mock(HttpServletRequest.class);
		given(request.getHeader("X-Real-IP")).willReturn(remoteHostFromX_REAL_IP);
		
		//When
		String result = HttpRequestUtil.getRemoteHost(request);
		
		//Then
		assertEquals(remoteHostFromX_REAL_IP, result);
	}
	
	@Test
	@DisplayName("HttpRequest의 RemoteHost를 반환 -> 'X-Real-IP' 헤더 없음")
	public void getRemoteHostTestNoX_REAL_IP() {
		//Given
		String remoteHost = "66.72.22.1";
		HttpServletRequest request = mock(HttpServletRequest.class);
		given(request.getHeader("X-Real-IP")).willReturn(null);
		given(request.getRemoteHost()).willReturn(remoteHost);
		
		//When
		String result = HttpRequestUtil.getRemoteHost(request);
		
		//Then
		assertEquals(remoteHost, result);
	}
}
