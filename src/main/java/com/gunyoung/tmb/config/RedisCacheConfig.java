package com.gunyoung.tmb.config;

import static com.gunyoung.tmb.utils.CacheConstants.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

/**
 * Redis를 이용한 캐시를 위한 설정 클래스 <br>
 * @Profile("dev") - Test에 반영안되도록 추가  
 * @author kimgun-yeong
 *
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
@Profile("dev")
public class RedisCacheConfig {
	
	/**
	 * Redis Session Storage와 Redis Cache Storage를 분리
	 * @author kimgun-yeong
	 */
	@Value("${redis.cache.host}")
	private String redisCacheHost;
	
	@Value("${redis.cache.port}")
	private int redisCachePort;
	
	/**
	 * Redis-Cli로 Lettuce 채택
	 * @return 
	 * @author kimgun-yeong
	 */
	@Bean(name="redisCacheConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisCacheHost,redisCachePort);
		return lettuceConnectionFactory;	
	}
	
	/**
	 * Redis Cache에 대한 설정들을 한데모아 RedisCacheManager Bean 생성
	 * @param redisConnectionfactorty
	 * @return
	 * @author kimgun-yeong
	 */
	@Bean
	public CacheManager redisCacheManager(@Qualifier("redisCacheConnectionFactory") RedisConnectionFactory redisConnectionfactorty) {
		RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionfactorty)
				.cacheDefaults(redisCacheConfiguration())
				.withInitialCacheConfigurations(detailCacheConfigurations())
				.build();
		
		return redisCacheManager;
	}
	
	/**
	 * 모든 REDIS CACHE에 대한 기본 설정
	 * @return
	 * @author kimgun-yeong
	 */
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(CACHE_DEFAULT_EXPIRE_MIN))
				.disableCachingNullValues()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				;
	}
	
	/**
	 * REDIS CACHE 항목들에 대한 디테일 설정 <br>
	 * User, Muscle, Exercise 객체 관련 설정들
	 * @return
	 * @author kimgun-yeong
	 */
	@Bean
	public Map<String,RedisCacheConfiguration> detailCacheConfigurations() {
		Map<String,RedisCacheConfiguration> cacheConfigurations= new HashMap<>();
		cacheConfigurations.put(MUSCLE_SORT_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(MUSCLE_SORT_EXPIRE_MIN)));
		cacheConfigurations.put(EXERCISE_SORT_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(EXERCISE_SORT_EXPIRE_MIN)));
		cacheConfigurations.put(COMMENT_LIKE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(COMMENT_LIKE_EXPIRE_MIN)));
		cacheConfigurations.put(POST_LIKE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(POST_LIKE_EXPIRE_MIN)));
		return cacheConfigurations;
	}
}
