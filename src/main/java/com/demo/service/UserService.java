package com.demo.service;

import java.time.LocalDate;
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
import com.demo.util.JWTUtil;

@Service
public class UserService {
	
	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private AppResponse response;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private JWTUtil util;
	public ResponseEntity<AppResponse> signup(RegistrationDto registration) throws UserExistException{
		Optional<User> checkUser = userRepository.findByEmail(registration.getEmail());
		if(checkUser.isPresent()) {
			response.setData(null);
			response.setMessage("email user sudah ada");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
			response.setData(null);
			response.setMessage("data user tidak ditemukan");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		if(!bcrypt.matches(login.getPassword(), user.get().getPassword())) {
			response.setMessage("user tidak valid");
			response.setData(null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		String tokenJwt = util.generateToken(user.get());
		Map<String, Object> map = new HashMap<>();
		map.put("data", user);
		map.put("jwt-token", tokenJwt);
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
			response.setData(null);
			response.setMessage("data user tidak ditemukan");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
		userRepository.deleteById(id);;
		response.setData(null);
		response.setMessage("sukses menghapus data user");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	public ResponseEntity<AppResponse> addPoint(User point, Long id){
		Optional<User> user = userRepository.findById(id);
		Map<String, Object> map = new HashMap<>();
		if(user.isEmpty()) {
			response.setData(null);
			response.setMessage("gagal mencari data user!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if(!user.get().isPremium()) {
			response.setData(null);
			response.setMessage("user tidak memiliki status premium!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		user.get().setPoints(user.get().getPoints() + point.getPoints());
		response.setData(null);
		response.setMessage("sukses menambahkan point user");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
	public ResponseEntity<AppResponse> addPremium(Long id, int month){
		Optional<User> user = userRepository.findById(id);
		if(user.isEmpty()) {
			response.setData(null);
			response.setMessage("gagal mencari data user!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		user.get().setPremium(true);;
		switch(month) {
			case 1:
				user.get().setPremium_expire(LocalDate.now().plusMonths(1));
				break;
			case 3:
				user.get().setPremium_expire(LocalDate.now().plusMonths(3));
				break;
			case 6:
				user.get().setPremium_expire(LocalDate.now().plusMonths(6));
				break;
			case 12:
				user.get().setPremium_expire(LocalDate.now().plusMonths(12));
				break;
		}
		userRepository.save(user.get());
		Map<String, Object> map = new HashMap<>();
		map.put("data", user);
		response.setMessage("sukses menambahkan status premium ke user");
		response.setData(map);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	public ResponseEntity<AppResponse> getPaymentDescription(){
		Map<String, Object> map = new HashMap<>();
		map.put("Bulan 1", 15000);
		map.put("Bulan 3", 40000);
		map.put("Bulan 6", 65000);
		map.put("Bulan 12", 105000);
		response.setMessage("sukses mendapatkan deskripsi pembayaran");
		response.setData(map);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
