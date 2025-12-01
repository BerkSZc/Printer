package com.berksozcu.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.berksozcu"})
@ComponentScan(basePackages = {"com.berksozcu"})
@EntityScan(basePackages = {"com.berksozcu"})
@EnableAsync
public class PrinterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrinterApplication.class, args);
	}

}
