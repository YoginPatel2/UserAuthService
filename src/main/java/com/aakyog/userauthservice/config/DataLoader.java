package com.aakyog.userauthservice.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aakyog.userauthservice.model.Role;
import com.aakyog.userauthservice.model.User;
import com.aakyog.userauthservice.repository.RoleRepository;
import com.aakyog.userauthservice.repository.UserRepository;

@Configuration
public class DataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner loadData() {
        return args -> {
            // Add roles
            Role adminRole = new Role("ADMIN","Admin role");
            Role userRole = new Role("USER", "User role");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            
            // Add users
            User admin = new User("yogin", passwordEncoder.encode("admin123"), "admin@example.com", "yogin patel","+9187464789" );
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            userRepository.save(admin);
        };
    }
}
