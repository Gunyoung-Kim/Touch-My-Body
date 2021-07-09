package com.gunyoung.tmb.config;

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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.gunyoung.tmb.domain.exercise.Muscle;
import com.gunyoung.tmb.utils.CacheUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableCaching
@RequiredArgsConstructor
@Profile("default")
public class RedisCacheConfig {
	
	@Value("${redis.cache.host}")
	private String redisCacheHost;
	
	@Value("${redis.cache.port}")
	private int redisCachePort;
	
	/**
	 * 
	 * @return
	 * @author kimgun-yeong
	 */
	@Bean(name="redisCacheConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisCacheHost,redisCachePort);
		return lettuceConnectionFactory;	
	}
	
	/**
	 * 
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
				.entryTtl(Duration.ofMinutes(CacheUtil.CACHE_DEFAULT_EXPIRE_MIN))
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
		cacheConfigurations.put(CacheUtil.USER_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CacheUtil.USER_EXPIRE_MIN)));
		cacheConfigurations.put(CacheUtil.MUSCLE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CacheUtil.MUSCLE_EXPIRE_MIN))
				// Muscle 객체에는 FetchType이 Lazy인 연관 엔티티가 없어서 Serialize 바로 가능
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<Muscle>(Muscle.class))));
		cacheConfigurations.put(CacheUtil.EXERCISE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CacheUtil.EXERCISE_EXPIRE_MIN)));
		cacheConfigurations.put(CacheUtil.COMMENT_LIKE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CacheUtil.COMMENT_LIKE_EXPIRE_MIN)));
		cacheConfigurations.put(CacheUtil.POST_LIKE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(CacheUtil.POST_LIKE_EXPIRE_MIN)));
		return cacheConfigurations;
	}
}
