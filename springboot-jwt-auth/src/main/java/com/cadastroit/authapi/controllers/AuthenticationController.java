package com.cadastroit.authapi.controllers;

import com.cadastroit.authapi.dtos.LoginUserDto;
import com.cadastroit.authapi.dtos.RegisterUserDto;
import com.cadastroit.authapi.entities.User;
import com.cadastroit.authapi.responses.LoginResponse;
import com.cadastroit.authapi.services.AuthenticationService;
import com.cadastroit.authapi.services.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    	
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
        
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
    	
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        return ResponseEntity.ok(new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime()));
        
    }
}