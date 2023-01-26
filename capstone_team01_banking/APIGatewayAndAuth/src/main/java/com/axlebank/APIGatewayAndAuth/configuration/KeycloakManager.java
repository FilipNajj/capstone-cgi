package com.axlebank.APIGatewayAndAuth.configuration;

import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Component;

@Component
public class KeycloakManager {

    private final KeycloakProperties keycloakProperties;

    public KeycloakManager(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    public RealmResource getKeyCloakInstanceWithRealm() {
        return keycloakProperties.getInstance().realm(keycloakProperties.getRealm());
    }
}
