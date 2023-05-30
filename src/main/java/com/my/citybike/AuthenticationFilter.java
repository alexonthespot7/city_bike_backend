package com.my.citybike;

/*
 * This class represents a filter used for authentication in the CityBike application.
 * It extends the OncePerRequestFilter class provided by Spring.
 */

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.my.citybike.model.User;
import com.my.citybike.model.UserRepository;
import com.my.citybike.service.AuthenticationService;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private AuthenticationService jwtService;

	@Autowired
	private UserRepository urepository;

	/*
	 * This method is invoked for each incoming request to perform authentication.
	 * It extracts the JWT token from the request header, retrieves the user details
	 * from the UserRepository, and creates an Authentication object based on the
	 * retrieved user. The Authentication object is then set in the
	 * SecurityContextHolder to establish the user's authentication status.
	 */

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		// Retrieve the JWT token from the request header
		String jws = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (jws != null) {
			// Extract the username from the JWT token
			String username = jwtService.getAuthUser(request);

			// Retrieve the user details from the UserRepository
			Optional<User> optUser = urepository.findByUsername(username);

			Authentication authentication;

			if (optUser.isPresent()) {
				User user = optUser.get();
				boolean enabled = user.isAccountVerified();
				// Create a custom user object for authentication
				MyUser myUser = new MyUser(user.getId(), username, user.getPassword(), enabled, true, true, true,
						AuthorityUtils.createAuthorityList(user.getRole()));
				// Create the authentication token
				authentication = new UsernamePasswordAuthenticationToken(myUser, null,
						AuthorityUtils.createAuthorityList(user.getRole()));
			} else {
				// If the user does not exist, create an empty authentication token
				authentication = new UsernamePasswordAuthenticationToken(null, null, Collections.emptyList());
			}

			// Set the authentication object in the SecurityContextHolder
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// Continue the filter chain
		filterChain.doFilter(request, response);
	}
}
