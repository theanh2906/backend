package com.example.backend.rest;

import com.example.backend.models.Role;
import com.example.backend.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @CachePut(value = "roles", key = "#role.id")
    @PostMapping("")
    public Role addRole(@RequestBody Role role) {
        return roleService.addNew(role);
    }

    @Cacheable(value = "roles")
    @GetMapping("")
    public List<Role> findAllRole() {
        LOG.error("All role found!!");
        return roleService.findAll();
    }

    @Cacheable(value = "roles", key = "#roleId")
    @GetMapping("/{roleId}")
    public Role findRoleById(@PathVariable Long roleId) {
        LOG.error("Getting role with ID {}.", roleId);
        return roleService.findById(roleId);
    }
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private RoleService roleService;
}
