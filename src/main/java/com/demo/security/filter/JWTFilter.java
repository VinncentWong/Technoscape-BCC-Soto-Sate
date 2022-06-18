package com.demo.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.security.authentication.JWTAuthentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String header = request.getHeader("Authorization");
			if(header == null || !header.startsWith("Bearer ")) {
				throw new ServletException("Format token tidak valid!");
			}
			header = header.substring(7,header.length());
			Claims claims = Jwts.parser()
								.parseClaimsJws(header)
								.getBody();
			String subject = claims.getSubject();
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("user"));
			SecurityContextHolder.getContext().setAuthentication(new JWTAuthentication(subject, null, authorities));
		}
		catch(ServletException ex) {
			throw new ServletException(ex);
		}
	}
}
