package com.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.dto.LoginDto;
import com.demo.dto.RegistrationDto;
import com.demo.entities.User;
import com.demo.exception.UserExistException;
import com.demo.exception.UserNotFoundException;
import com.demo.repositories.UserRepository;
import com.demo.util.AppResponse;

@Service
public class UserService {
	
	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private AppResponse response;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	public ResponseEntity<AppResponse> signup(RegistrationDto registration) throws UserExistException{
		Optional<User> checkUser = userRepository.findByEmail(registration.getEmail());
		if(checkUser.isPresent()) {
			throw new UserExistException("email sudah ada di database");
		}
		User user = new User();
		user.setEmail(registration.getEmail());
		user.setPassword(
				bcrypt.encode(registration.getPassword())
				);
		user.setUsername(registration.getName());
		user.setCreated_at(new Date());
		user.setPremium_expire(null);
		user.setPremium(false);
		userRepository.save(user);
		Map<String, Object> map = new HashMap<>();
		map.put("data", user);
		response.setMessage("Success add user data");
		response.setData(map);
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
	
	public ResponseEntity<AppResponse> getUser(Long id) throws UserNotFoundException{
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
		Map<String, Object> map = new HashMap<>();
		map.put("data", user);
		response.setMessage("sukses menemukan data user");
		response.setData(map);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	public ResponseEntity<AppResponse> updateUser(User userBody, Long id)throws UserNotFoundException{
		Optional<User> user = userRepository.findById(id);
		if(user.isEmpty()) {
			throw new UserNotFoundException("data user tidak ditemukan!");
		}
		if(userBody.getEmail() != null) {
			user.get().setEmail(userBody.getEmail());
		}
		if(userBody.getUsername() != null) {
			user.get().setUsername(userBody.getUsername());
		}
		if(userBody.getPassword() != null) {
			user.get().setPassword(userBody.getPassword());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", user);
		user.get().setUpdated_at(new Date());
		response.setMessage("sukses mengupdate data user");
		response.setData(map);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	public ResponseEntity<AppResponse> deleteUser(Long id){
		userRepository.deleteById(id);
		response.setData(null);
		response.setMessage("sukses menghapus data user");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
