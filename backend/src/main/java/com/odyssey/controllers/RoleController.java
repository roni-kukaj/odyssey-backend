package com.odyssey.controllers;

import com.odyssey.models.Role;
import com.odyssey.dtos.RoleRegistrationRequest;
import com.odyssey.services.RoleService;
import com.odyssey.dtos.RoleUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasAuthority('MAINADMIN') or hasAuthority('ADMIN')")
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasAuthority('MAINADMIN') or hasAuthority('ADMIN')")
    @GetMapping("/{roleId}")
    public Role getRoleById(@PathVariable("roleId") Integer roleId) {
        return roleService.getRole(roleId);
    }

    @PreAuthorize("hasAuthority('MAINADMIN')")
    @PostMapping
    public void registerRole(@RequestBody RoleRegistrationRequest request) {
        roleService.addRole(request);
    }

    @PreAuthorize("hasAuthority('MAINADMIN')")
    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable("roleId") Integer roleId) {
        roleService.deleteRole(roleId);
    }

    @PreAuthorize("hasAuthority('MAINADMIN')")
    @PutMapping("/{roleId}")
    public void updateRole(@PathVariable("roleId") Integer roleId, @RequestBody RoleUpdateRequest request) {
        roleService.updateRole(roleId, request);
    }

}
