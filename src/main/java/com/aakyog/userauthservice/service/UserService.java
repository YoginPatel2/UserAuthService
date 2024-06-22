package com.aakyog.userauthservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aakyog.userauthservice.model.Role;
import com.aakyog.userauthservice.model.User;
import com.aakyog.userauthservice.repository.RoleRepository;
import com.aakyog.userauthservice.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    // Method to save or update a user
    public void saveUser(User user) {
        // Encode password before saving
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }

    // Method to find a user by username
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }

    // Method to check if a username exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Method to check if an email exists
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Method to delete a user by username
    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    // Method to retrieve all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void assignRolesToUser(User user, Set<Role> roles1) {

        // Fetch roles from the database by their names
    	Set<String> rolesSet = roles1.stream().map(role -> role.getName()).collect(Collectors.toSet());
        Set<Role> roles = roleRepository.findByNameIn(rolesSet);

        // Clear existing roles to avoid duplication and add new roles
        user.getRoles().clear();
        user.getRoles().addAll(roles);

        // Save the user entity
        userRepository.save(user);
    }
    // Other methods as needed

}
