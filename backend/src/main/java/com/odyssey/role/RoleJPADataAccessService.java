package com.odyssey.role;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("roleJPAService")
public class RoleJPADataAccessService implements RoleDao {

    private final RoleRepository roleRepository;

    public RoleJPADataAccessService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> selectAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> selectRoleById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public void insertRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public boolean existsRoleById(Integer id) {
        return roleRepository.existsRoleById(id);
    }

    @Override
    public boolean existsRoleByName(String name) {
        return roleRepository.existsRoleByName(name);
    }

    @Override
    public void deleteRoleById(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void updateRole(Role role) {
        roleRepository.save(role);
    }
}
