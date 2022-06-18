package com.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.LoginDto;
import com.demo.dto.RegistrationDto;
import com.demo.exception.UserNotFoundException;
import com.demo.service.UserService;
import com.demo.util.AppResponse;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	public ResponseEntity<AppResponse> signup(@RequestBody @Valid RegistrationDto dto){
		return userService.signup(dto);
	}
	
	public ResponseEntity<AppResponse> login(@RequestBody @Valid LoginDto login) throws UserNotFoundException{
		return userService.login(login);
	}
}
