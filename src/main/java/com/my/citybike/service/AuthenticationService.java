/*
 * This class provides authentication-related functionality, including token generation and retrieving the authenticated user from the request.
 * It is responsible for generating JSON Web Tokens (JWT) for authentication purposes.
 */
package com.my.citybike.service;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthenticationService {
	static final long EXPIRATION_TIME = 864_000_00; // Token expiration time set to 24 hours
	static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secret key used for signing the JWT
	static final String PREFIX = "Bearer"; // Prefix used in the Authorization header to indicate the presence of a JWT

	/*
	 * Generates a JWT token for the given username. The token contains the username
	 * as the subject and has an expiration time.
	 */
	public String getToken(String username) {
		String token = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(key).compact();
		return token;
	}

	/*
	 * Retrieves the authenticated user from the provided HttpServletRequest. It
	 * parses the JWT token from the Authorization header, verifies it using the
	 * secret key, and retrieves the subject (username) from the token's claims.
	 */
	public String getAuthUser(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (token != null) {
			String user = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.replace(PREFIX, ""))
					.getBody().getSubject();

			if (user != null)
				return user;
		}

		return null;
	}
}