package com.my.citybike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.my.citybike.model.User;
import com.my.citybike.model.UserRepository;

@SpringBootApplication
public class CityBikeApplication {
	@Autowired
	private UserRepository uRepository;

	public static void main(String[] args) {
		SpringApplication.run(CityBikeApplication.class, args);
	}
	
	/**@Bean
	public CommandLineRunner runner() {
		return (args) -> {
			uRepository.save(new User("admin", "$2a$12$jBJVWZnVf6cyPcUW1UJTye1ke2RHlileJFrtp59ADQbZsSYqErrOy", "ADMIN",
					"mrbudach@mail.ru", true));
		};
	}*/

}
