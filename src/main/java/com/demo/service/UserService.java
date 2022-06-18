package com.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.dto.LoginDto;
import com.demo.dto.RegistrationDto;
import com.demo.entities.User;
import com.demo.exception.UserNotFoundException;
import com.demo.repositories.UserRepository;
import com.demo.util.AppResponse;

@Service
public class UserService {
	
	@Autowired
	private AppResponse response;
	
	@Autowired
	private UserRepository userRepository;
	
	public ResponseEntity<AppResponse> signup(RegistrationDto registration){
		User user = new User();
		user.setEmail(registration.getEmail());
		user.setPassword(registration.getPassword());
		user.setUsername(registration.getName());
		user.setCreated_at(new Date());
		userRepository.save(user);
		Map<String, Object> map = new HashMap<>();
		map.put("data", user);
		response.setMessage("Success add user data");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	public ResponseEntity<AppResponse> login(LoginDto login) throws UserNotFoundException{
		Optional<User> user = userRepository.findByEmail(login.getEmail());
		if(user.isEmpty()) {
			throw new UserNotFoundException("data user tidak ditemukan!");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", user);
		response.setData(map);
		response.setMessage("sukses menemukan data user!");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
