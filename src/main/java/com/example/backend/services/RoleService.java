package com.example.backend.services;

import com.example.backend.models.Role;
import com.example.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    public Role addNew(Role newRole) {
        return roleRepository.save(newRole);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(new Role());
    }

    @Autowired
    private RoleRepository roleRepository;
}
