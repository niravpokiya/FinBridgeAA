package com.FinBridgeAA.fiu_usecase_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FiuUsecaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiuUsecaseServiceApplication.class, args);
	}

}
