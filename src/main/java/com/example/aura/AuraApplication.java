package com.example.aura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// Exclude DataSource auto-configuration since we're using Firebase
@SpringBootApplication(scanBasePackages = "com.example.aura")
public class AuraApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuraApplication.class, args);
	}

}
