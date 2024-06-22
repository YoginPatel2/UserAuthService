package com.aakyog.userauthservice.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aakyog.userauthservice.dto.RoleDto;
import com.aakyog.userauthservice.dto.UserDto;
import com.aakyog.userauthservice.model.Role;
import com.aakyog.userauthservice.model.User;
import com.aakyog.userauthservice.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", ""})
    public ResponseEntity<?> loadingDefault(){
    	return ResponseEntity.ok("Users api is working..");
    }

    
    // Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        // Validate input, create User entity from DTO
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getFullName(), userDto.getPhoneNumber());
        // Add default role or logic to assign roles
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Endpoint to get a user by username
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update user details
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserDto userDto) {
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            // Update user entity with new data from DTO
            existingUser.setEmail(userDto.getEmail());
            existingUser.setFullName(userDto.getFullName());
            existingUser.setPhoneNumber(userDto.getPhoneNumber());
            // Optionally, update other fields as needed

            userService.saveUser(existingUser);
            return ResponseEntity.ok("User updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to delete a user by username
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok("User deleted successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to retrieve all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // Example of adding roles to a user (assuming RoleService and RoleDto are defined)
    @PutMapping("/{username}/roles")
    public ResponseEntity<?> assignRolesToUser(@PathVariable String username, @RequestBody List<RoleDto> roleDtos) {
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            // Convert RoleDto objects to Role entities and add to user roles
            Set<Role> roles = roleDtos.stream()
                    .map(roleDto -> new Role(roleDto.getName(), roleDto.getDescription()))
                    .collect(Collectors.toSet());

            existingUser.setRoles(roles); // Set roles to user

            userService.assignRolesToUser(existingUser, roles);
            return ResponseEntity.ok("Roles assigned to user successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Other controller methods as needed

}
