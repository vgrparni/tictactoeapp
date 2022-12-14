package com.tictactoe.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class, scanBasePackages = { "com.tictactoe.app.*"})
public class TicTacToeAppApplication {
	 private static final Logger log = LoggerFactory.getLogger(TicTacToeAppApplication.class);
	public static void main(String[] args) {
		log.info("----Running tictactoeapp service---- ");
		SpringApplication.run(TicTacToeAppApplication.class, args);
	}

}
