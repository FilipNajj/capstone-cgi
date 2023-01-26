package com.axlebank.APIGatewayAndAuth.configuration;


import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProperties {

    @Value("${app.config.keycloak.url}")
    private String serverUrl;

    @Value("${app.config.keycloak.realm}")
    private String realm;

    @Value("${app.config.keycloak.client-id}")
    private String clientId;

    @Value("${app.config.keycloak.client-secret}")
    private String clientSecret;

    private static Keycloak keycloakInstance = null;

    public Keycloak getInstance() {

        if (keycloakInstance == null) {
            keycloakInstance = KeycloakBuilder
                    .builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                    .username("axlebank-admin")
//                    .password("cKdb0S3CHrG4")
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        return keycloakInstance;
    }

    public String getRealm() {
        return realm;
    }

}
