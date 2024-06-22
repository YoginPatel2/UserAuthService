package com.aakyog.userauthservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aakyog.userauthservice.model.Role;
import com.aakyog.userauthservice.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	// Method to save or update a role
	public void saveRole(Role role) {
		roleRepository.save(role);
	}

	// Method to find a role by name
	public Role findByName(String name) {
		Optional<Role> roleOptional = roleRepository.findByName(name);
		return roleOptional.orElse(null);
	}

	// Method to delete a role by name
	public void deleteRoleByName(String name) {
		roleRepository.deleteByName(name);
	}

	// Method to retrieve all roles
	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

	// Other methods as needed

}
