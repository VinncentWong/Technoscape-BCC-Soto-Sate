package com.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.LoginDto;
import com.demo.dto.RegistrationDto;
import com.demo.entities.User;
import com.demo.exception.UserExistException;
import com.demo.exception.UserNotFoundException;
import com.demo.service.UserService;
import com.demo.util.AppResponse;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<AppResponse> signup(@RequestBody @Valid RegistrationDto dto) throws UserExistException{
		return userService.signup(dto);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AppResponse> login(@RequestBody @Valid LoginDto login) throws UserNotFoundException{
		return userService.login(login);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<AppResponse> get(@PathVariable Long id) throws UserNotFoundException{
		return userService.getUser(id);
	}
	
	@PatchMapping("/update/{id}")
	public ResponseEntity<AppResponse> update(@RequestBody User user,@PathVariable Long id) throws UserNotFoundException{
		return userService.updateUser(user, id);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<AppResponse> delete(@PathVariable Long id){
		return userService.deleteUser(id);
	}
}
