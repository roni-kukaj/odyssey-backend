package com.odyssey.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{roleId}")
    public Role getRoleById(@PathVariable("roleId") Integer roleId) {
        return roleService.getRole(roleId);
    }

    @PostMapping
    public void registerRole(@RequestBody RoleRegistrationRequest request) {
        roleService.addRole(request);
    }

    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable("roleId") Integer roleId) {
        roleService.deleteRole(roleId);
    }

    @PutMapping("/{roleId}")
    public void updateRole(@PathVariable("roleId") Integer roleId, @RequestBody RoleUpdateRequest request) {
        roleService.updateRole(roleId, request);
    }

}
