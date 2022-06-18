package com.demo.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.demo.entities.User;

import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {
	
	public String generateToken(User user) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", user.getId());
		map.put("username", user.getUsername());
		map.put("email", user.getEmail());
		String token = Jwts.builder()
							.setSubject(user.getUsername())
							.setClaims(map)
							.compact();
		return token;
	}
}
