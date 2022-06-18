package com.demo.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.demo.entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	private static final String JWT_SECRET = "SECRET_JWT_FOR_A_WHILE";
	
	public String generateToken(User user) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", user.getId());
		map.put("username", user.getUsername());
		map.put("email", user.getEmail());
		String token = Jwts.builder()
							.signWith(SignatureAlgorithm.HS256, 
									Base64.encode(JWT_SECRET.getBytes()))
							.setSubject(user.getUsername())
							.setClaims(map)
							.compact();
		return token;
	}
	
	public String getKey() {
		return this.JWT_SECRET;
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
