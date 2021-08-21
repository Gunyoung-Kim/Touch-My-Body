package com.gunyoung.tmb.config.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunyoung.tmb.config.RedisSessionConfig;

/**
 * {@link RedisSessionConfig} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) RedisSessionConfig only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class RedisSessionConfigUnitTest {
	
	private String redisSessionHost = "127.0.0.1";
	
	private int redisSessionPort = 6379;
	
	@InjectMocks 
	RedisSessionConfig redisSessionConfig;
	
	@Mock
	ObjectMapper objectMapper;
	
	/*
	 * public RedisConnectionFactory redisConnectionFactory()
	 */
	
	@Test
	@DisplayName("RedisConnectionFactory 생성 -> LettuceConnectionFactory 생성")
	public void redisConnectionFactoryTestLettuce() throws NoSuchFieldException, IllegalAccessException {
		//Given
		settingRedisSessionConfigField();
		
		//When
		RedisConnectionFactory result = redisSessionConfig.redisConnectionFactory();
		
		//Then
		assertTrue(result instanceof LettuceConnectionFactory);
	}
	
	/*
	 * public RedisTemplate<String, Object> redisTemplate()
	 */

	@Test
	@DisplayName("RedisTemplate 빈 생성 -> KeySerializer 확인")
	public void redisTemplateTestKeySerializer()  throws NoSuchFieldException, IllegalAccessException{
		//Given
		settingRedisSessionConfigField();
		
		//When
		RedisTemplate<String, Object> result = redisSessionConfig.redisTemplate();
		
		//Then
		assertTrue(result.getKeySerializer() instanceof StringRedisSerializer);
	}
	
	@Test
	@DisplayName("RedisTemplate 빈 생성 -> ValueSerializer 확인")
	public void redisTemplateTestValueSerializer()  throws NoSuchFieldException, IllegalAccessException{
		//Given
		settingRedisSessionConfigField();
		
		//When
		RedisTemplate<String, Object> result = redisSessionConfig.redisTemplate();
		
		//Then
		assertTrue(result.getValueSerializer() instanceof GenericJackson2JsonRedisSerializer);
	}
	
	/*
	 * public ConfigureRedisAction configureRedisAction()
	 */
	
	@Test
	@DisplayName("ConfigureRedisAction 빈 생성 -> NO_OP 인지 확인")
	public void configureRedisActionTestNO_OP() {
		//Given
		
		//When
		ConfigureRedisAction result = redisSessionConfig.configureRedisAction();
		
		//Then
		assertEquals(ConfigureRedisAction.NO_OP, result);
	}
	
	private void settingRedisSessionConfigField() throws NoSuchFieldException, IllegalAccessException {
		Field redisSessionHostField = redisSessionConfig.getClass().getDeclaredField("redisSessionHost");
		redisSessionHostField.setAccessible(true);
		redisSessionHostField.set(redisSessionConfig, redisSessionHost);
		
		Field redisSessionPortField = redisSessionConfig.getClass().getDeclaredField("redisSessionPort");
		redisSessionPortField.setAccessible(true);
		redisSessionPortField.set(redisSessionConfig, redisSessionPort);
	}
}
