package com.odyssey.role;

import java.util.Optional;

import java.util.List;

public interface RoleDao {
    List<Role> selectAllRoles();
    Optional<Role> selectRoleById(Integer id);
    void insertRole(Role role);
    void updateRole(Role role);
    boolean existsRoleById(Integer id);
    boolean existsRoleByName(String name);
    void deleteRoleById(Integer id);
}
