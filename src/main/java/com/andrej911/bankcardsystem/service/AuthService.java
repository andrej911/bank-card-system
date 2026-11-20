package com.andrej911.bankcardsystem.service;

import com.andrej911.bankcardsystem.dto.LoginRequest;
import com.andrej911.bankcardsystem.dto.LoginResponse;
import com.andrej911.bankcardsystem.dto.SignupRequest;
import com.andrej911.bankcardsystem.exception.ResourceAlreadyExistsException;
import com.andrej911.bankcardsystem.model.Role;
import com.andrej911.bankcardsystem.model.User;
import com.andrej911.bankcardsystem.repository.UserRepository;
import com.andrej911.bankcardsystem.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalStateException("Authenticated user has no authorities"));

        return new LoginResponse(jwt, userDetails.getUsername(), role);
    }

    @Transactional
    public User registerUser(SignupRequest signUpRequest) {

        String email = signUpRequest.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Username is already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
    }
}