package com.titanium.practicingspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracticingspringApplication {

	private static final String POSTGRESDB = "postgres";
	private static final String MSSQLBD = "mssql";

	public static void main(String[] args) {

		System.setProperty("spring.profiles.active", MSSQLBD); // Change the contnstant final propertie for the one
																	// you're using
		SpringApplication.run(PracticingspringApplication.class, args);

	}
}
