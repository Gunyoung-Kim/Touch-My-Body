package com.gunyoung.tmb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Redis Session을 위한 설정 클래스 <br>
 * 세션 만료 시간은 1시간 - 운동 기록 작성 고려
 * @Profile("dev") - Test에 반영되지 않기 위해 추가
 * @author kimgun-yeong
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=3600)
@RequiredArgsConstructor
@Profile("dev")
public class RedisSessionConfig {
	
	private final ObjectMapper objectMapper;
	
	/**
	 * Redis Session Storage, Redis Cache Storage 분리
	 * @author kimgun-yeong
	 */
	@Value("${redis.session.host}")
	private String redisSessionHost;
	
	@Value("${redis.session.port}")
	private int redisSessionPort;
	
	/**
	 * REDIS-CLi로 LETTUCE 채택
	 * @author kimgun-yeong
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisSessionHost,redisSessionPort);
	}
	
	/**
	 * 오브젝트 serialization 과 connection management <br>
	 * key serializer : stringSerializer <br>
	 * value serializer : genericjsonSerializer
	 * @author kimgun-yeong
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
		return redisTemplate;
	}
	
	/**
	 * AWS ElastiCache 같은 secured redis 환경에서 <br>
	 * {@link org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction} 가 Redis config 명령어 수행하지 않게 설
	 * @author kimgun-yeong
	 */
	@Bean
	public ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}
}
