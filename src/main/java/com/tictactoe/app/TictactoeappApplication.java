package com.tictactoe.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = { "com.tictactoe.app.*"})
public class TictactoeappApplication {
	 private static final Logger log = LoggerFactory.getLogger(TictactoeappApplication.class);
	public static void main(String[] args) {
		log.info("----Running tictactoeapp service---- ");
		SpringApplication.run(TictactoeappApplication.class, args);
	}

}
