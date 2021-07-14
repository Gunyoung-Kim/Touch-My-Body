package com.gunyoung.tmb.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컨트롤러 메소드에서 로그인된 유저의 ID를 세션에서 가져오는 로직이 있다면 이 어노테이션을 적용해 <br>
 * 세션에 해당 값이 null이 아닌지 확인
 * @author kimgun-yeong
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginIdSessionNotNull {
	
}
