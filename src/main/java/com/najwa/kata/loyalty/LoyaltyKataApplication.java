package com.najwa.kata.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class LoyaltyKataApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoyaltyKataApplication.class, args);
	}

}
