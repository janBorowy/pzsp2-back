package com.example.timetraderapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
// exclude disable security module, to turn on - delete
public class TimeTraderApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeTraderApiApplication.class, args);
	}

}
