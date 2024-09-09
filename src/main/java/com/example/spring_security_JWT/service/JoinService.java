package com.example.spring_security_JWT.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring_security_JWT.dto.JoinDTO;
import com.example.spring_security_JWT.entity.UserEntity;
import com.example.spring_security_JWT.repository.UserRepository;

@Service
public class JoinService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	//생성자 방식으로 초기화?
	public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public void joinProcess(JoinDTO joinDTO){
		String username = joinDTO.getUsername();
		String password = joinDTO.getPassword();

		//repository에 해당 username을 갖는 user여부 확인
		Boolean isExist = userRepository.existsByUsername(username);
		if(isExist){
			return;
		}

		//repository
		UserEntity data = new UserEntity();
		data.setUsername(username);
		data.setPassword(bCryptPasswordEncoder.encode(password));
		data.setRole("ROLE_ADMIN");
		userRepository.save(data);
	}
}
