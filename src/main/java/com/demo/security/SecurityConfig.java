package com.demo.security;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.demo.security.filter.CustomUsernamePasswordFilter;
import com.demo.security.filter.JWTFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/user/login", "/user/signup");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().permitAll();
		// add filter or change filter with custom filter
		http.addFilterAt(new CustomUsernamePasswordFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(new JWTFilter(), CustomUsernamePasswordFilter.class);
		http.csrf().disable();
		http.cors(c -> {
			CorsConfigurationSource src = (request) -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(List.of("*"));
				config.setAllowedHeaders(List.of("*"));
				config.setAllowedMethods(List.of("*"));
				return config;
			};
			c.configurationSource(src);
		});
	}

}
