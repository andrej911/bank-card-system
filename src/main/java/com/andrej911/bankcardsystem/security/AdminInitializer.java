package com.andrej911.bankcardsystem.security;

import com.andrej911.bankcardsystem.model.Role;
import com.andrej911.bankcardsystem.model.User;
import com.andrej911.bankcardsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String email;
    private final String password;

    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            @Value("${app.admin.username}") String username,
                            @Value("${app.admin.email}") String email,
                            @Value("${app.admin.password:}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (password == null || password.isBlank()) {
            log.info("Admin bootstrap skipped: ADMIN_PASSWORD is not set");
            return;
        }
        
        if (password.length() < 8) {
            throw new IllegalStateException("app.admin.password must be at least 8 characters");
        }

        if (userRepository.existsByUsername(username)) {
            log.info("Admin user '{}' already exists, skipping", username);
            return;
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setEmail(email.trim().toLowerCase(java.util.Locale.ROOT));
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(Role.ROLE_ADMIN);
        userRepository.save(admin);

        log.info("Admin user '{}' created", username);
    }
}
