package com.axlebank.APIGatewayAndAuth.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        http
                .authorizeExchange(exchange -> exchange
                //ALLOWING REGISTER API FOR DIRECT ACCESS
                .pathMatchers("/api/v1/auth/register", "/api/v1/auth/users/*").permitAll()
                .pathMatchers("/api/v1/bankproducts", "/api/v1/bankproducts/*").permitAll()
                                .pathMatchers("/api/v1/company/product").permitAll()
//                .pathMatchers("/api/v1/accounts", "/api/v1/accounts/*").permitAll()
//                .pathMatchers("/api/v1/company", "/api/v1/company/*").permitAll()
                .pathMatchers("/", "/*.css", "/*.js", "/favicon.ico", "/assets/*").permitAll()
                //ALL OTHER APIS ARE AUTHENTICATED
                .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf().disable()
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessHandler(
                        oidcLogoutSuccessHandler(clientRegistrationRepository)));
//                .oauth2ResourceServer()

//                .jwt();
        return http.build();
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(
            ReactiveClientRegistrationRepository clientRegistrationRepository
    ) {
        var oidcLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(
                        clientRegistrationRepository);
        oidcLogoutSuccessHandler
                .setPostLogoutRedirectUri("{baseUrl}");
        return oidcLogoutSuccessHandler;
    }
}
