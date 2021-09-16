package com.gunyoung.tmb.aop.log.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gunyoung.tmb.aop.log.LogAspect;

/**
 * {@link LogAspect} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) LogAspect only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class LogAspectUnitTest {
	
	@Mock
	Logger logger;
	
	@InjectMocks
	LogAspect logAspect;
	
	/*
	 * public Object loggingAroundController(ProceedingJoinPoint pjp) throws Throwable 
	 */
	
	@Test
	@DisplayName("컨트롤러에 Request 가 들어오면 Request 메소드,uri, parameters, remote address, 처리 시간 들을 로깅 -> 정상, Parameter not empty")
	public void loggingAroundControllerTestParamNotEmpty() throws Throwable{
		//Given
		Map<String, String[]> paramMap = getParameterMap();
		mockingRequestContextHolderCurrentRequestAttributes(paramMap);
		ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class); 
		
		//When
		logAspect.loggingAroundController(pjp);
		
		//Then
		then(logger).should(times(1)).info(anyString(),any(),any(), any(), any(), any());
	}
	
	private Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramMap = new HashMap<>();
		paramMap.put("id", new String[]{"test@test.com"});
		paramMap.put("password", new String[] {"abcd1234!"});
		return paramMap;
	}
	
	@Test
	@DisplayName("컨트롤러에 Request 가 들어오면 Request 메소드,uri, parameters, remote address, 처리 시간 들을 로깅 -> 정상, Parameter empty")
	public void loggingAroundControllerTestParamEmpty() throws Throwable{
		//Given
		Map<String, String[]> paramMap = getEmptyParameterMap();
		mockingRequestContextHolderCurrentRequestAttributes(paramMap);
		ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class); 
		
		//When
		logAspect.loggingAroundController(pjp);
		
		//Then
		then(logger).should(times(1)).info(anyString(),any(),any(), any(), any(), any());
	}
	
	private Map<String, String[]> getEmptyParameterMap() {
		return new HashMap<>();
	}
	
	private void mockingRequestContextHolderCurrentRequestAttributes(Map<String, String[]> paramMap) {
		RequestAttributes attributes = mockingRequestAttributesGetRequest(paramMap);
		RequestContextHolder.setRequestAttributes(attributes);
	}
	
	private RequestAttributes mockingRequestAttributesGetRequest(Map<String, String[]> paramMap) {
		HttpServletRequest request = mockingHttpRequestServletGetParameterMap(paramMap);
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(request);
		return servletRequestAttributes;
	}
	
	private HttpServletRequest mockingHttpRequestServletGetParameterMap(Map<String, String[]> paramMap) {
		HttpServletRequest request = mock(HttpServletRequest.class);
		given(request.getParameterMap()).willReturn(paramMap);
		given(request.getMethod()).willReturn("GET");
		given(request.getRequestURI()).willReturn("/login");
		given(request.getRemoteHost()).willReturn("0:0:0:0:0:0:0:1");
		return request;
	}
	
}
