package com.my.citybike.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);

	@Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
	Optional<User> findByEmail(String email);
}
