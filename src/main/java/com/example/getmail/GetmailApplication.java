package com.example.getmail;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@MapperScan("com.example.getmail.mapper")
@SpringBootApplication
public class GetmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetmailApplication.class, args);
	}

}
