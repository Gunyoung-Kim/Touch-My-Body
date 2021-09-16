package com.gunyoung.tmb.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import com.gunyoung.tmb.security.UserAuthenticationProvider;
import com.gunyoung.tmb.security.UserDetailsServiceImpl;
import com.gunyoung.tmb.services.domain.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Spring-Security 관련 설정 클래스
 * @author kimgun-yeong
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserService userService;
	
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	
	private final LogoutSuccessHandler logoutSuccessHandler;
	
	/**
	 * 
	 * @author kimgun-yeong
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/user/**").hasRole("USER")
			.antMatchers("/manager/**").hasRole("MANAGER")
			.antMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().permitAll();
		
		http.formLogin()
			.loginPage("/login").permitAll()
			.successHandler(authenticationSuccessHandler);
		
		http.logout()
			.logoutSuccessHandler(logoutSuccessHandler);
	}
	
	@Override 
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.authenticationProvider(authenticationProvider());
	}
	
	/**
	 * 유저의 권한 체계 설정 <br>
	 * 관리자(ADMIN) > 매니저(MANAGER) > 일반 유저(USER)
	 * @return RoleHierarchy 객체 - 유저 권환 계급 체계 반환 
	 * @author kimgun-yeong
	 */
	@Bean
	public RoleHierarchy roleHierarchy() {
		Map<String,List<String>> roleHierarchyMap = new HashMap<>();
		roleHierarchyMap.put("ROLE_ADMIN", Arrays.asList("ROLE_MANAGER"));
		roleHierarchyMap.put("ROLE_MANAGER", Arrays.asList("ROLE_USER"));
		
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
		roleHierarchy.setHierarchy(roles);
		
		return roleHierarchy;
	}
	
	/**
	 * 유저의 권한 체계 추가 
	 * @return
	 * @author kimgun-yeong
	 */
	@Bean
	public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
		DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		return webSecurityExpressionHandler;
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl(userService);
	}
	
	/**
	 * Thymeleaf에서 <sec:authorize> 네임 스페이스 같은 확장 기능을 사용하기 위한 Bean 
	 * @author kimgun-yeong
	 */
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
	
	@Bean
	public UserAuthenticationProvider authenticationProvider() {
		return new UserAuthenticationProvider(userDetailsService(),passwordEncoder());
	}
	
	/**
	 * Password 암호화 위한 빈
	 * @author kimgun-yeong
	 */
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
