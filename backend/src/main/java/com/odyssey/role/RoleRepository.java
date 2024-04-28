package com.odyssey.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsRoleById(Integer id);
    boolean existsRoleByName(String name);
}
