package com.my.citybike;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.my.citybike.model.User;
import com.my.citybike.model.UserRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserCreation() {
        User user = new User("username", "password", "role", "email@example.com", true);
        userRepository.save(user);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void testFindByUsername() {
        User user = new User("username", "password", "role", "email@example.com", true);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("username");
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo("email@example.com");
    }

    @Test
    public void testFindByEmail() {
        User user = new User("username", "password", "role", "email@example.com", true);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("email@example.com");
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getUsername()).isEqualTo("username");
    }
}