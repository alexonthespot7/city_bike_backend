/*
 * This class represents a custom user implementation extending the Spring Security User class.
 * It provides additional functionality by including a user ID along with the standard user details.
 */
package com.my.citybike;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

public class MyUser extends User {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Long id;

	/*
	 * Constructs a new MyUser object with the specified user ID, username, password,
	 * authorities, and other authentication-related flags.
	 */
	public MyUser(Long id, String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
	}

	/*
	 * Constructs a new MyUser object with the specified user ID, username, password,
	 * and authorities.
	 */
	public MyUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
	}

	/*
	 * Returns the ID associated with this user.
	 */
	public Long getId() {
		return id;
	}
}
