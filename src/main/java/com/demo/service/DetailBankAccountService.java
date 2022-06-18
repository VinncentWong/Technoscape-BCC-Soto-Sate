package com.demo.service;

import java.io.IOException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.entities.DetailBankAccount;
import com.demo.util.AppResponse;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class DetailBankAccountService {
	
private static final String CLIENT_ID = "1eba780f-291a-47b5-8056-b8a0774cfc9f";
	
	private static final String CLIENT_SECRET = "H3r6dksNmFFKxPgjyODIxqGzmzzSnN";
	
	private static final String BASE_URL = "http://sandbox.onebrick.io/";
	
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private AppResponse response;
    
	public ResponseEntity<AppResponse> getAccessToken() throws Exception{
		//JsonNode to store the json data retrieved
        JsonNode result = MAPPER.createObjectNode();
        try {
            //client to acces sandbox
            var client = HttpClientBuilder.create().build();
            //request to perform GET method
            var request = new HttpGet(BASE_URL + "/v1/auth/token");
            //authentication header
            var authHeader = CLIENT_ID + ":" + CLIENT_SECRET;
            //encoded authentication
            var encHeader = Base64.encodeBase64(authHeader.getBytes(StandardCharsets.ISO_8859_1));
            authHeader ="Basic " + new String(encHeader);
            request.setHeader("Authorization", authHeader);
            //execute Get Request
            var response = client.execute(request);
            var node = MAPPER.readValue(response.getEntity().getContent(), ObjectNode.class);
            //if condition to store the data
            if(node.has("data")) {
                result = node.get("data");
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("result", result);
        response.setData(map);
        response.setMessage("sukses mendapatkan informasi akun anda");
        return ResponseEntity.status(HttpStatus.OK).body(response);
        //return json data with public access token from Brick API
	}
	
	public ResponseEntity<AppResponse> getAccountDetail() throws DataBindingException, StreamReadException, DatabindException, UnsupportedOperationException, IOException{
		String jwtToken = request.getHeader("Authorization");
		jwtToken = jwtToken.substring(7, jwtToken.length());
		//create new object mapper
        var mapper = new ObjectMapper();
        //link to the account list
        var url = BASE_URL + "v1/account/list";
        var client = HttpClientBuilder.create().build();
        var request = new HttpGet(url);
            //request header for the http
        request.setHeader("Authorization", "Bearer " + jwtToken);
        var responseHttp = client.execute(request);
        var node = mapper.readValue(responseHttp.getEntity().getContent(), ObjectNode.class);
        DetailBankAccount[] accounts = mapper.convertValue(node.get("data"), DetailBankAccount[].class);
        Map<String, Object> map = new HashMap<>();
            //condition when there is no account in the list
        if(accounts.length == 0) {
        	map.put("data", new DetailBankAccount());
        	response.setData(map);
        	response.setMessage("sukses mendapatkan detail akun");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        //condition when account list exist
        } else {
        	map.put("data", accounts[0]);
        	response.setData(map);
        	response.setMessage("sukses mendapatkan detail akun");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
	}
}
