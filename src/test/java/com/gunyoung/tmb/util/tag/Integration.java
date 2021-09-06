package com.gunyoung.tmb.util.tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Tag;

/**
 * 통합 테스트 클래스에 추가하는 어노테이션 <br>
 * 테스트 커버리지 검사 시 통합 테스트 클래스는 제외하기 위함
 * @author kimgun-yeong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Tag("integration")
public @interface Integration {

}
