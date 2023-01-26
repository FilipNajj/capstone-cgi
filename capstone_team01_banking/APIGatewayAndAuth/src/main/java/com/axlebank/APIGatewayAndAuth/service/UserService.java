package com.axlebank.APIGatewayAndAuth.service;

import com.axlebank.APIGatewayAndAuth.client.AdminServiceClient;
import com.axlebank.APIGatewayAndAuth.client.ClientServiceClient;
import com.axlebank.APIGatewayAndAuth.client.CreditCardServiceClient;
import com.axlebank.APIGatewayAndAuth.exception.UserAlreadyRegisteredException;
import com.axlebank.APIGatewayAndAuth.exception.UserCreationException;
import com.axlebank.APIGatewayAndAuth.exception.UserDoesntExist;
import com.axlebank.APIGatewayAndAuth.model.dto.*;
import com.axlebank.APIGatewayAndAuth.model.entity.UserEntity;
import com.axlebank.APIGatewayAndAuth.model.mapper.UserMapper;
import com.axlebank.APIGatewayAndAuth.model.repository.UserRepository;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@Service
public class UserService {

    private final KeycloakUserService keycloakUserService;
    private final UserRepository userRepository;

    private final ClientServiceClient clientServiceClient;

    private final AdminServiceClient adminServiceClient;

    private final CreditCardServiceClient creditCardServiceClient;

    @Autowired
    private RestTemplate restTemplate;

    public UserService(KeycloakUserService keycloakUserService, UserRepository userRepository,
                       ClientServiceClient clientServiceClient, AdminServiceClient adminServiceClient, CreditCardServiceClient creditCardServiceClient) {
        this.keycloakUserService = keycloakUserService;
        this.userRepository = userRepository;
        this.clientServiceClient = clientServiceClient;
        this.adminServiceClient = adminServiceClient;
        this.creditCardServiceClient = creditCardServiceClient;
    }

    private UserMapper userMapper = new UserMapper();

    public User createUser(User user) throws UserAlreadyRegisteredException {
        System.out.println(user);
        List<UserRepresentation> userRepresentations = keycloakUserService.readUserByEmail(user.getEmail());
        if (userRepresentations.size() > 0) {
            throw new UserAlreadyRegisteredException("This email already registered as a user. Please check and retry.");
        }


            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEmailVerified(false);
            if(user.getRole().equals(Role.admin) || user.getRole().equals(Role.creditCardCompany)){
                userRepresentation.setEnabled(false);
            }else {
                userRepresentation.setEnabled(true);
            }
            userRepresentation.setUsername(user.getEmail());

//            userRepresentation.setRealmRoles(singletonList(user.getRole().toString()));

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setValue(user.getPassword());
            credentialRepresentation.setTemporary(false);
            userRepresentation.setCredentials(singletonList(credentialRepresentation));

            Integer userCreationResponse = keycloakUserService.createUser(userRepresentation);

            if (userCreationResponse == 201) {
                System.out.println("User created under given username: " +  user.getEmail());
                keycloakUserService.assignRole(user.getEmail(), user.getRole().toString());

                List<UserRepresentation> userRepresentations1 = keycloakUserService.readUserByEmail(user.getEmail());
                user.setAuthId(userRepresentations1.get(0).getId());
                if(user.getRole().equals(Role.admin) || user.getRole().equals(Role.creditCardCompany)){
                    user.setStatus(Status.PENDING);
                } else {
                    user.setStatus(Status.APPROVED);
                }
//                user.setIdentification(userResponse.getIdentificationNumber());
                UserEntity save = userRepository.save(userMapper.convertToEntity(user));

                if(user.getRole().equals(Role.client)) {
                    sendToClientService( save.getId().intValue(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getAddress());
                } else if (user.getRole().equals(Role.creditCardCompany)) {
                    sendToCreditCardCompanyService(save.getId().intValue(), user.getCompanyName(), user.getEmail(), user.getAddress());
                } else {
                    sendToAdminService( save.getId().intValue(), user.getFirstName(), user.getLastName(), user.getEmail());
                }

                System.out.println("Id of the newly created user: " +  save.getId());

                return userMapper.convertToDto(save);
            }

        throw new UserCreationException("User creation problem");

    }

    public User userExists(String emailAddress) throws UserDoesntExist{
        List<UserRepresentation> userRepresentations = keycloakUserService.readUserByEmail(emailAddress);
        User user = new User();
        if (userRepresentations.size() == 0) {
            throw new UserDoesntExist("This email address is not registered");
        } else {

            user.setEmail(userRepresentations.get(0).getEmail());
            user.setFirstName(userRepresentations.get(0).getFirstName());
            user.setLastName(userRepresentations.get(0).getLastName());
            return user;
        }
    }

    public List<User> readUsers(Pageable pageable) {
        Page<UserEntity> allUsersInDb = userRepository.findAll(pageable);
        List<User> users = userMapper.convertToDtoList(allUsersInDb.getContent());
        users.forEach(user -> {
            UserRepresentation userRepresentation = keycloakUserService.readUser(user.getAuthId());
            user.setEmail(userRepresentation.getEmail());
            });
        return users;
    }

    public Long getId(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return userEntity.getId();
    }

    public User updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (userUpdateRequest.getStatus() == Status.APPROVED) {
            UserRepresentation userRepresentation = keycloakUserService.readUser(userEntity.getAuthId());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);
            keycloakUserService.updateUser(userRepresentation);
        }

        userEntity.setStatus(userUpdateRequest.getStatus());
        return userMapper.convertToDto(userRepository.save(userEntity));
    }

    public void sendToClientService(int profileId, String firstName, String lastName, String email, Address address) {
        Client client = new Client(profileId, firstName, lastName, email, address);
        System.out.println(client);
        Client client1 = clientServiceClient.register(client);
    }

    public void sendToCreditCardCompanyService (int companyId, String companyName, String email, Address address){
        CreditCardCompany creditCardCompany = new CreditCardCompany(companyId, companyName, email, address);
        System.out.println(creditCardCompany);
        CreditCardCompany creditCardCompany1 = creditCardServiceClient.register(creditCardCompany);
    }

    public void sendToAdminService (int adminId, String firstName, String lastName, String email){
        Admin admin = new Admin(adminId, firstName, lastName, email);
        System.out.println(admin);
        Admin admin1 = adminServiceClient.register(admin);
    }

}
