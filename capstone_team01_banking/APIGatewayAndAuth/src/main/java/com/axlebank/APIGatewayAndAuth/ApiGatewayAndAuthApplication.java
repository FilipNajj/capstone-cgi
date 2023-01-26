package com.axlebank.APIGatewayAndAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;


@SpringBootApplication
@EnableFeignClients
//@EnableDiscoveryClient
public class ApiGatewayAndAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayAndAuthApplication.class, args);
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
