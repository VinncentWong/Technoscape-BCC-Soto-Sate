package com.demo.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DataBindingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.demo.entities.DetailBankAccount;
import com.demo.util.AppResponse;
import com.demo.util.JWTUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class DetailBankAccountService {
	
private static final String CLIENT_ID = "1eba780f-291a-47b5-8056-b8a0774cfc9f";
	
	private static final String CLIENT_SECRET = "H3r6dksNmFFKxPgjyODIxqGzmzzSnN";
	
	private static final String BASE_URL = "http://sandbox.onebrick.io/";
	
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private static final String PUBLIC_ACCESS_TOKEN = "public-sandbox-880f6d5d-3a27-423c-939e-306d94a031cd";
    
    private static final String USER_ACCESS_TOKEN = "";
    
    @Autowired
    private AppResponse response;
    
    @Autowired
    private JWTUtil util;
    
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
	
	public void callBrickWidget() throws RestClientException, Exception{
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://sandbox.onebrick.io/sandbox-widget/v1/?accessToken=public-sandbox-48ea9d22-3f77-4f71-bb51-2c710fd889cd&redirect_url=/api/getusertoken"))
			    .header("Accept", "application/json")
			    .method("GET", HttpRequest.BodyPublishers.noBody())
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
	}
}
