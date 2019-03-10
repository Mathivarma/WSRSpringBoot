package com.mathi.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mathi.ControllerWSRCount.ControllerWSR;;
@SpringBootApplication
public class MathiWsrApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathiWsrApplication.class, args);
	}

	@Bean
	ControllerWSR ControllerWSR() {
		return new ControllerWSR();
	}
}
