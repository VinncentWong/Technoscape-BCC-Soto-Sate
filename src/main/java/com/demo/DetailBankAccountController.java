package com.demo;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DataBindingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entities.DetailBankAccount;
import com.demo.service.DetailBankAccountService;
import com.demo.util.AppResponse;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/api")
public class DetailBankAccountController{
	
	@Autowired
	private DetailBankAccountService service;
    
	@GetMapping("/getaccesstoken")
	public ResponseEntity<AppResponse> getAccessToken() throws Exception{
		return service.getAccessToken();
	}
	
	public ResponseEntity<AppResponse> getAccountDetail() throws DataBindingException, StreamReadException, DatabindException, UnsupportedOperationException, IOException{
		return service.getAccountDetail();
	}
}
