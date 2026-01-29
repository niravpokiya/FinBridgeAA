package com.FinBridgeAA.Account_aggregator_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountAggregatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountAggregatorServiceApplication.class, args);
	}

}
