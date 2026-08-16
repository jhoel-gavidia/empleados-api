package com.jhoel.empleados_api.config;

import com.jhoel.empleados_api.entity.Role;
import com.jhoel.empleados_api.entity.User;
import com.jhoel.empleados_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.initialize:false}")
    private boolean initializeAdmin;

    @Value("${app.admin.username:}")
    private String adminUsername;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Bean
    CommandLineRunner initializeAdmin() {
        return args -> {

            if (!initializeAdmin) {
                return;
            }

            if (adminUsername.isBlank()
                    || adminEmail.isBlank()
                    || adminPassword.isBlank()) {
                throw new IllegalStateException(
                        "Admin initialization is enabled but admin credentials are missing"
                );
            }

            boolean usernameExists = userRepository
                    .findByUsernameOrEmail(adminUsername)
                    .isPresent();

            boolean emailExists = userRepository
                    .findByUsernameOrEmail(adminEmail)
                    .isPresent();

            if (usernameExists || emailExists) {
                return;
            }

            User admin = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        };
    }
}