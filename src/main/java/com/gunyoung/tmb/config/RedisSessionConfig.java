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
 * 세션 만료 시간은 1시간 - 운동 기록 작성 고려
 * @author kimgun-yeong
 *
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=3600)
@RequiredArgsConstructor
@Profile("dev")
public class RedisSessionConfig {
	
	private final ObjectMapper objectMapper;
	
	@Value("${redis.session.host}")
	private String redisSessionHost;
	
	@Value("${redis.session.port}")
	private int redisSessionPort;
	
	/**
	 * REDIS-CLi로 LETTUCE 
	 * @return
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisSessionHost,redisSessionPort);
		return lettuceConnectionFactory;
	}
	
	/**
	 * 오브젝트 serialization 과 connection management <br>
	 * key serializer : stringSerializer <br>
	 * value serializer : genericjsonSerializer
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
		return redisTemplate;
	}
	
	@Bean
	public ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}
}
