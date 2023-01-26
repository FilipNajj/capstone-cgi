package com.axlebank.APIGatewayAndAuth.controller;

import com.axlebank.APIGatewayAndAuth.exception.UserAlreadyRegisteredException;
import com.axlebank.APIGatewayAndAuth.exception.UserDoesntExist;
import com.axlebank.APIGatewayAndAuth.model.dto.LoggedUser;
import com.axlebank.APIGatewayAndAuth.model.dto.User;
import com.axlebank.APIGatewayAndAuth.model.dto.UserUpdateRequest;
import com.axlebank.APIGatewayAndAuth.service.KeycloakUserService;
import com.axlebank.APIGatewayAndAuth.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final KeycloakUserService keycloakUserService;
    private final UserService userService;

    public UserController(KeycloakUserService keycloakUserService, UserService userService) {
        this.keycloakUserService = keycloakUserService;
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity createUser(@RequestBody User request) {
        System.out.println("Creating user with: " + request);
        try {
            return new ResponseEntity(userService.createUser(request), HttpStatus.CREATED);
        } catch (UserAlreadyRegisteredException e) {
            return new ResponseEntity("User Already Registered", HttpStatus.CONFLICT);
        }
    }

//    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
//    public String login(@RequestBody User request) {
//        return keycloakUserService.login(request.getEmail(), request.getPassword());
//    }

//    @GetMapping("/isAuthenticated")
//    public HashMap valid(@RequestHeader("Authorization") String authHeader) {
//        try {
//            keycloakUserService.checkValidity(authHeader);
//            return new HashMap (){{
//                put("is_valid", "true");
//            }};
//        } catch (Exception e) {
//            System.out.println("token is not valid, exception : {} "+  e.getMessage());
//            return new HashMap (){{
//                put("is_valid", "false");
//            }};
//        }
//    }

    @GetMapping("/user")
    public Mono<LoggedUser> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        Long id = 0l;
        if (oidcUser.getEmail() != null ) {
            id = userService.getId(oidcUser.getEmail());
        }
        var user = new LoggedUser(
                id,
                oidcUser.getPreferredUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getClaimAsStringList("roles")
        );
        return Mono.just(user);
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity userExist(@PathVariable("userName") String userName){

        try {
            User user = userService.userExists(userName);
            return ResponseEntity.ok(user);
        } catch (UserDoesntExist e) {
            return new ResponseEntity("Not Found", HttpStatus.NOT_FOUND);
        }

    }


//    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Map logout(@RequestParam("refresh_token") String refreshToken) {
//        try {
//            keycloakUserService.logout(refreshToken);
//            return new HashMap (){{
//                put("logout", "true");
//            }};
//        } catch (Exception e) {
//            System.out.println("unable to logout, exception : {} " + e.getMessage());
//            return new HashMap (){{
//                put("logout", "false");
//            }};
//        }
//    }

    @PatchMapping(value = "/update/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        System.out.println("Updating user with {}" + userUpdateRequest.toString());
        return ResponseEntity.ok(userService.updateUser(userId, userUpdateRequest));
    }

//    @GetMapping
//    public ResponseEntity readUsers(Pageable pageable) {
//        System.out.println("Reading all users from API");
//        return ResponseEntity.ok(userService.readUsers(pageable));
//    }

//    @GetMapping(value = "/{id}")
//    public ResponseEntity readUser(@PathVariable("id") Long id) {
//        System.out.println("Reading user by id {}" + id);
//        return ResponseEntity.ok(userService.readUser(id));
//    }

}
