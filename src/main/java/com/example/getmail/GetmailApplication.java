package com.example.getmail;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.example.getmail.mapper")
@SpringBootApplication
public class GetmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetmailApplication.class, args);
	}

}
