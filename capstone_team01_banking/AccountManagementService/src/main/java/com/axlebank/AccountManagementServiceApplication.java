package com.axlebank;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.axlebank.controller.AccountController;

@SpringBootApplication
@EnableFeignClients
public class AccountManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountManagementServiceApplication.class, args);
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
	   
	   System.out.println("cors invoked");
	
	    CorsConfiguration corsConfig = new CorsConfiguration();
	    corsConfig.setAllowedOrigins(Arrays.asList("*"));
	    corsConfig.setMaxAge(3600L);
	    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST","PUT","DELETE"));
	    corsConfig.addAllowedHeader("*");
	
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfig);
	
	    return new CorsWebFilter(source);
	}

	
}
