package com.titanium.practicingspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerSpringApplication {

	private static final String POSTGRESDB = "postgres";
	private static final String MSSQLBD = "mssql"; // Solo para pruebas en MsSQl

	public static void main(String[] args) {

		System.setProperty("spring.profiles.active", MSSQLBD);
		SpringApplication.run(ServerSpringApplication.class, args);

	}
}
