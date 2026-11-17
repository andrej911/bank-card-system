package com.andrej911.bankcardsystem.controller;

import com.andrej911.bankcardsystem.dto.LoginRequest;
import com.andrej911.bankcardsystem.dto.LoginResponse;
import com.andrej911.bankcardsystem.dto.SignupRequest;
import com.andrej911.bankcardsystem.model.User;
import com.andrej911.bankcardsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
    
    private final AuthService authService;

  
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
        
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {    
       
    	LoginResponse response = authService.authenticateUser(loginRequest); 
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody SignupRequest signUpRequest) {
        
        User user = authService.registerUser(signUpRequest);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)  
                .body("User registered successfully! Username: " + user.getUsername());
    }
}