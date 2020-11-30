package com.eatza.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Config Server application
 * 
 * @author Swati Raj
 * @since 1.0.0
 *
 */
@EnableConfigServer
@EnableEurekaClient
@SpringBootApplication
public class ConfigServerBootApplication {

	/**
	 * Main method to run spring boot application
	 * 
	 * @param args args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerBootApplication.class, args);
	}
}
