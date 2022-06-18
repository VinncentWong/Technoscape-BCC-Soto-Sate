package com.demo.controller;

import java.io.IOException;
import javax.xml.bind.DataBindingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.demo.service.DetailBankAccountService;
import com.demo.util.AppResponse;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

@RestController
@RequestMapping("/api")
public class DetailBankAccountController{
	
	@Autowired
	private DetailBankAccountService service;
    
	@GetMapping("/getaccesstoken")
	public ResponseEntity<AppResponse> getAccessToken() throws Exception{
		return service.getAccessToken();
	}
	
	@GetMapping("/getwidget")
	public void callBrickWidget() throws RestClientException, Exception{
		service.callBrickWidget();
	}
	
	@PostMapping("/getusertoken")
	public void token() {}
}
