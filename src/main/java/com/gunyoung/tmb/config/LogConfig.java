package com.gunyoung.tmb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gunyoung.tmb.aop.log.LogAspect;

/**
 * 로그 관련 Configuration 클래스
 * @author kimgun-yeong
 *
 */
@Configuration
public class LogConfig {
	
	/**
	 * LogAspect에서 사용할 Logger 빈 등록
	 * @author kimgun-yeong
	 */
	@Bean
	public Logger logger() {
		return LoggerFactory.getLogger(LogAspect.class);
	}
}
