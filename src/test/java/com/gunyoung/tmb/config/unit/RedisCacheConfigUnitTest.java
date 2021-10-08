package com.gunyoung.tmb.config.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import com.gunyoung.tmb.config.RedisCacheConfig;
import com.gunyoung.tmb.utils.CacheConstants;

/**
 * {@link RedisCacheConfig} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) RedisCacheConfig only
 * {@link org.mockito.BDDMockito}를 활용한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class RedisCacheConfigUnitTest {
	
	@InjectMocks
	RedisCacheConfig redisCacheConfig;
	
	private String redisCacheHost = "127.0.0.1";
	
	private int redisCachePort = 6379;
	
	/*
	 * public RedisConnectionFactory redisConnectionFactory()
	 */
	
	@Test
	@DisplayName("RedisConnectionFactory 생성 -> LettuceConnectionFactory 생성")
	public void redisConnectionFactoryTestLettuce() throws NoSuchFieldException, IllegalAccessException {
		//Given
		Field redisCacheHostField = redisCacheConfig.getClass().getDeclaredField("redisCacheHost");
		redisCacheHostField.setAccessible(true);
		redisCacheHostField.set(redisCacheConfig, redisCacheHost);
		
		Field redisCachePortField = redisCacheConfig.getClass().getDeclaredField("redisCachePort");
		redisCachePortField.setAccessible(true);
		redisCachePortField.set(redisCacheConfig, redisCachePort);
		
		//When
		RedisConnectionFactory result = redisCacheConfig.redisConnectionFactory();
		
		//Then
		assertTrue(result instanceof LettuceConnectionFactory);
	}
	
	/*
	 * public CacheManager redisCacheManager(@Qualifier("redisCacheConnectionFactory") RedisConnectionFactory redisConnectionfactorty)
	 */
	
	@Test
	@DisplayName("RedisCacheManager 빈 생성 -> RedisCacheManager 인지 확인")
	public void redisCacheManagerTest() {
		//Given
		RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);
		
		//When
		CacheManager result = redisCacheConfig.redisCacheManager(redisConnectionFactory);
		
		//Then
		assertTrue(result instanceof RedisCacheManager);
	}
	
	/*
	 * public RedisCacheConfiguration redisCacheConfiguration()
	 */
	
	@Test
	@DisplayName("Redis Cahce에 대한 설정 확인 -> default ttl 확인")
	public void redisCacheConfigurationTestTtl() {
		//Given
		Duration entryTtl = Duration.ofMinutes(CacheConstants.CACHE_DEFAULT_EXPIRE_MIN);
		
		//When
		RedisCacheConfiguration result = redisCacheConfig.redisCacheConfiguration();
		
		//Then
		assertEquals(entryTtl, result.getTtl());
	}
	
	@Test
	@DisplayName("Redis Cahce에 대한 설정 확인 -> null값 캐싱 불가능 확인")
	public void redisCacheConfigurationTestIsNullValueAllowed() {
		//Given
		boolean isNullValueAllowed = false;
		
		//When
		RedisCacheConfiguration result = redisCacheConfig.redisCacheConfiguration();
		
		//Then
		assertEquals(isNullValueAllowed, result.getAllowCacheNullValues());
	}
}
