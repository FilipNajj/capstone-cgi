package com.axlebank.APIGatewayAndAuth.service;

import com.axlebank.APIGatewayAndAuth.configuration.KeycloakManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeycloakUserService {


    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.config.keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${app.config.keycloak.user-info-uri}")
    private String keycloakUserInfo;

    @Value("${app.config.keycloak.logout}")
    private String keycloakLogout;

    @Value("${app.config.keycloak.client-id}")
    private String clientId;

    @Value("${app.config.keycloak.authorization-grant-type}")
    private String grantType;

    @Value("${app.config.keycloak.client-secret}")
    private String clientSecret;

    @Value("${app.config.keycloak.scope}")
    private String scope;
    private final KeycloakManager keyCloakManager;

    public KeycloakUserService(KeycloakManager keyCloakManager) {
        this.keyCloakManager = keyCloakManager;
    }


    /**
     *  login by using username and password to keycloak, and capturing token on response body
     *
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username",username);
        map.add("password",password);
        map.add("client_id",clientId);
        map.add("grant_type",grantType);
        map.add("client_secret",clientSecret);
        map.add("scope",scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        System.out.println(request);
        return restTemplate.postForObject(keycloakTokenUri, request, String.class);
    }

    /**
     *  a successful user token will generate http code 200, other than that will create an exception
     *
     * @param token
     * @return
     * @throws Exception
     */
    public String checkValidity(String token) throws Exception {
        return getUserInfo(token);
    }

    private String getUserInfo(String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        System.out.println(request);
        return restTemplate.postForObject(keycloakUserInfo, request, String.class);
    }

    /**
     *  logging out and disabling active token from keycloak
     *
     * @param refreshToken
     */
    public void logout(String refreshToken) throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id",clientId);
        map.add("client_secret",clientSecret);
        map.add("refresh_token",refreshToken);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        System.out.println(request);
        restTemplate.postForObject(keycloakLogout, request, String.class);
    }

    public List<String> getRoles(String token) throws Exception {
        String response = getUserInfo(token);

        // get roles
        Map map = new ObjectMapper().readValue(response, HashMap.class);
        return (List<String>) map.get("roles");
    }


    /**Managing Users
     *
     * @param userRepresentation
     * @return
     *
     *
     */
    public Integer createUser(UserRepresentation userRepresentation) {
        Response response = keyCloakManager.getKeyCloakInstanceWithRealm().users().create(userRepresentation);
        return response.getStatus();
    }

    public void assignRole(String userName, String role) {

        System.out.println(userName);

        String userId = keyCloakManager.getKeyCloakInstanceWithRealm()
                .users()
                .search(userName)
                .get(0)
                .getId();

        List<RoleRepresentation> roleList = rolesToRealmRoleRepresentation(Collections.singletonList(role));


        keyCloakManager.getKeyCloakInstanceWithRealm()
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(roleList);

    }

    public void updateUser(UserRepresentation userRepresentation) {
        keyCloakManager.getKeyCloakInstanceWithRealm().users().get(userRepresentation.getId()).update(userRepresentation);
    }


    public List<UserRepresentation> readUserByEmail(String email) {
        System.out.println(email);
        return keyCloakManager.getKeyCloakInstanceWithRealm().users().search(email);
    }


    public UserRepresentation readUser(String authId) {
        try {
            UserResource userResource = keyCloakManager.getKeyCloakInstanceWithRealm().users().get(authId);
            return userResource.toRepresentation();
        } catch (Exception e) {
            throw new EntityNotFoundException("User not found under given ID");
        }
    }

    private List<RoleRepresentation> rolesToRealmRoleRepresentation(List<String> roles) {
        List<RoleRepresentation> existingRoles = keyCloakManager.getKeyCloakInstanceWithRealm()
                .roles()
                .list();

        List<String> serverRoles = existingRoles
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        List<RoleRepresentation> resultRoles = new ArrayList<>();

        for (String role : roles) {
            int index = serverRoles.indexOf(role);
            if (index != -1) {
                resultRoles.add(existingRoles.get(index));
            } else {
                System.out.println("Role doesn't exist");
            }
        }
        return resultRoles;
    }
}
