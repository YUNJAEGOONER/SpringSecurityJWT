package com.example.spring_security_JWT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.spring_security_JWT.jwt.LoginFilter;

@Configuration //스프링의 환경 설정 파일임을 의미
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만듬
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration){
		this.authenticationConfiguration = authenticationConfiguration;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
		return configuration.getAuthenticationManager();
	}

	//비밀번호를 hash로 암호화
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

		//csrf disable
		http
			.csrf((auth) -> auth.disable());

		//form 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		//http basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		//경로별 인가 작업
		http
			.authorizeHttpRequests((auth)->auth
				.requestMatchers("/login", "/", "/join").permitAll() //login, root, join 경로에 대해서는 모든 권한을 허용
				.requestMatchers("/admin").hasRole("ADMIN") //admin 경로는 ADMIN이라는 권한을 가진 사용자만 접근 가능
				.anyRequest().authenticated());//나머지 경로에 대해서는 로그인 한 사용자만이 접근 가능

		http
			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

		//세션 설정 (JWT에서는 세션을 stateless 상태로 관리)
		http
			.sessionManagement((session)-> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
