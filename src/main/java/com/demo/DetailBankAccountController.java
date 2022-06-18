package com.demo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.util.AppResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/api")
public class DetailBankAccountController{
	
	private static final String CLIENT_ID = "1eba780f-291a-47b5-8056-b8a0774cfc9f";
	
	private static final String CLIENT_SECRET = "H3r6dksNmFFKxPgjyODIxqGzmzzSnN";
	
	private static final String BASE_URL = "http://sandbox.onebrick.io/";
	
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private AppResponse response;
    
	@GetMapping("/getaccesstoken")
	public ResponseEntity<AppResponse> getAccountDetail() throws Exception{
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
}
