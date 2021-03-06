package com.demo.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.security.authentication.JWTAuthentication;
import com.demo.util.JWTUtil;

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
								.setSigningKey(Base64.encode(new JWTUtil().getKey().getBytes()))
								.parseClaimsJws(header)
								.getBody();
			String subject = claims.getSubject();
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("user"));
			SecurityContextHolder.getContext().setAuthentication(new JWTAuthentication(subject, null, authorities));
			filterChain.doFilter(request, response);
		}
		catch(ServletException ex) {
			throw new ServletException(ex);
		}
	}
}
