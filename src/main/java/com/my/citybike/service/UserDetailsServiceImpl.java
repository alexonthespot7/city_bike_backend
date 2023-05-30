/*
 * This class implements the UserDetailsService interface from Spring Security.
 * It is responsible for loading user-specific data during the authentication process.
 * In this implementation, it retrieves user details from the UserRepository based on the provided username.
 */
package com.my.citybike.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.my.citybike.MyUser;
import com.my.citybike.model.User;
import com.my.citybike.model.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository urepository;

	/*
	 * Loads user-specific data based on the provided username. It retrieves the
	 * user details from the UserRepository and constructs a MyUser object which
	 * implements the UserDetails interface provided by Spring Security. If the user
	 * is not found, it throws a UsernameNotFoundException.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = urepository.findByUsername(username);

		MyUser myUser = null;

		if (user.isPresent()) {
			User currUser = user.get();

			boolean enabled = currUser.isAccountVerified();

			myUser = new MyUser(currUser.getId(), username, currUser.getPassword(), enabled, true, true, true,
					AuthorityUtils.createAuthorityList(currUser.getRole()));
		} else {
			throw new UsernameNotFoundException("User (" + username + ") not found.");
		}

		return myUser;
	}
}