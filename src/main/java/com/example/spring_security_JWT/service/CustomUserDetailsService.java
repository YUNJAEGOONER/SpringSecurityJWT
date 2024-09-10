package com.example.spring_security_JWT.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.spring_security_JWT.dto.CustomUserDetails;
import com.example.spring_security_JWT.entity.UserEntity;
import com.example.spring_security_JWT.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	private CustomUserDetailsService(UserRepository userRepository){
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//조회 진행
		UserEntity userData = userRepository.findByUsername(username);
		
		//조회한 데이터 검증 진행
		if(userData != null){
			return new CustomUserDetails(userData);
		}
		return null;
	}
}
