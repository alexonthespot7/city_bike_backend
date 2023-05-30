package com.my.citybike;

/*
 * This class represents the authentication entry point for the CityBike application.
 * It implements the AuthenticationEntryPoint interface provided by Spring.
 */

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

	/*
	 * This method is invoked when an authentication error occurs during the request
	 * processing. It sets the response status to SC_UNAUTHORIZED (401), indicating
	 * unauthorized access. It also sets the response content type to JSON and
	 * writes an error message indicating the authentication exception.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// Set the response status to unauthorized
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// Set the response content type to JSON
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// Get the PrintWriter to write the error message
		PrintWriter writer = response.getWriter();
		writer.println("Error: " + authException.getMessage());
	}
}