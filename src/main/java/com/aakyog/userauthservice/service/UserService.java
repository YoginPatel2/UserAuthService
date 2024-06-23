package com.aakyog.userauthservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aakyog.userauthservice.model.Role;
import com.aakyog.userauthservice.model.User;
import com.aakyog.userauthservice.repository.RoleRepository;
import com.aakyog.userauthservice.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// Method to save or update a user
    public void saveUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> isExisingUser =  userRepository.findByUsername(username);
		
		if (isExisingUser.isPresent()) {
            User user = isExisingUser.get();
            
            Set<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());
            
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
                // Add authorities/roles if required
            );
            return userDetails;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
	}

}
