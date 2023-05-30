package com.my.citybike.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.my.citybike.forms.AccountCredentials;
import com.my.citybike.model.User;
import com.my.citybike.model.UserRepository;
import com.my.citybike.service.AuthenticationService;

@RestController
public class UserController {
	@Autowired
	private UserRepository urepository;

	@Autowired
	private AuthenticationService jwtService;

	@Autowired
	AuthenticationManager authenticationManager;

	/**
	 * Endpoint for user login and token retrieval.
	 * 
	 * @param credentials The user account credentials.
	 * @return ResponseEntity containing the authentication token in the response
	 *         header if successful, or an error message with the corresponding HTTP
	 *         status code.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
		Optional<User> userByMail = urepository.findByEmail(credentials.getUsername());

		UsernamePasswordAuthenticationToken creds;

		if (userByMail.isPresent()) {
			if (userByMail.get().isAccountVerified()) {
				creds = new UsernamePasswordAuthenticationToken(userByMail.get().getUsername(),
						credentials.getPassword());
			} else {
				// Return error response if email is not verified
				return new ResponseEntity<>("Email is not verified", HttpStatus.CONFLICT);
			}
		} else {
			if (urepository.findByUsername(credentials.getUsername()).isPresent()) {
				if (urepository.findByUsername(credentials.getUsername()).get().isAccountVerified()) {
					creds = new UsernamePasswordAuthenticationToken(credentials.getUsername(),
							credentials.getPassword());
				} else {
					// Return error response if email is not verified
					return new ResponseEntity<>("Email is not verified", HttpStatus.CONFLICT);
				}
			} else {
				// Return error response for bad credentials
				return new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);
			}
		}

		Authentication auth = authenticationManager.authenticate(creds);

		String jwts = jwtService.getToken(auth.getName());

		Optional<User> currentUser = urepository.findByUsername(auth.getName());
		if (currentUser.isPresent()) {
			// Return successful response with token and user information in the headers
			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
					.header(HttpHeaders.ALLOW, currentUser.get().getRole())
					.header(HttpHeaders.HOST, currentUser.get().getId().toString())
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, Allow", "Host").build();
		} else {
			// Return error response for bad credentials
			return new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);
		}

	}
}