package com.andrej911.bankcardsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class BankCardSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankCardSystemApplication.class, args);
	}
}
