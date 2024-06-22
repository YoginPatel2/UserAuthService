package com.aakyog.userauthservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aakyog.userauthservice.dto.RoleDto;
import com.aakyog.userauthservice.model.Role;
import com.aakyog.userauthservice.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        // Validate input, create Role entity from DTO
        Role role = new Role(roleDto.getName(), roleDto.getDescription());
        roleService.saveRole(role);
        return ResponseEntity.ok("Role created successfully!");
    }

    @GetMapping("/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Role role = roleService.findByName(name);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Other controller methods as needed
}
