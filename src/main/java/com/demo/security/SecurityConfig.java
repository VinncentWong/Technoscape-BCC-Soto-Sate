package com.demo.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.demo.security.filter.CustomUsernamePasswordFilter;
import com.demo.security.filter.JWTFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Resource
	private CorsFilter corsFilter;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/user/login", "/user/signup", "/user/getpaymentdescription");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated();
		// add filter or change filter with custom filter
		http.addFilterAt(new CustomUsernamePasswordFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(new JWTFilter(), CustomUsernamePasswordFilter.class);
		http.csrf().disable();
		http.addFilterBefore(corsFilter, ChannelProcessingFilter.class);
		http.cors(c -> {
			CorsConfigurationSource src = (request) -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(List.of("*"));
				config.setAllowedHeaders(List.of("*"));
				config.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE"));
				return config;
			};
			c.configurationSource(src);
		});
	}

	@Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(1800L);
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
  
	@Bean
	public BCryptPasswordEncoder getBcrypt() {
		return new BCryptPasswordEncoder();
	}
}
