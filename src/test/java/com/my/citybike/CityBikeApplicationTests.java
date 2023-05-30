package com.my.citybike;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.citybike.web.MainController;
import com.my.citybike.web.UserController;

@SpringBootTest
class CityBikeApplicationTests {
	@Autowired
	private MainController mainController;
	
	@Autowired
	private UserController userController;
	
	@Test
	void contextLoads() throws Exception {
		assertThat(mainController).isNotNull();
	}
	
	@Test
	void contextLoadsTwo() throws Exception {
		assertThat(userController).isNotNull();
	}
}
