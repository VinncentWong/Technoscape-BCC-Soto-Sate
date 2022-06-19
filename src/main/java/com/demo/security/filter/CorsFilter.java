package com.demo.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter{

    // This is to be replaced with a list of domains allowed to access the server
	private final List<String> allowedOrigins = Arrays.asList("http://localhost:3000");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        /// Lets make sure that we are working with HTTP (that is, against HttpServletRequest and HttpServletResponse objects)
		if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;

			// Access-Control-Allow-Origin
			String origin = request.getHeader("Origin");
			response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "");

			// Access-Control-Allow-Credentials
			response.setHeader("Access-Control-Allow-Credentials", "true");

			// Access-Control-Allow-Methods
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE");

			// Access-Control-Allow-Headers
			response.setHeader("Access-Control-Allow-Headers",
				"*");
		}
		chain.doFilter(req, res);
    }
    
}
