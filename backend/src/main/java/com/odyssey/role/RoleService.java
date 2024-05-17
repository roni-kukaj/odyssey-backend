package com.odyssey.role;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleDao roleDao;

    public RoleService(@Qualifier("roleJPAService") RoleDao roleDao){
        this.roleDao = roleDao;
    }

    public List<Role> getAllRoles() {
        return roleDao.selectAllRoles();
    }

    public Role getRole(Integer id) {
        return roleDao.selectRoleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role with id [%s] not found".formatted(id)));
    }

    public void addRole(RoleRegistrationRequest request) {
        if (roleDao.existsRoleByName(request.name())) {
            throw new DuplicateResourceException("role already exists");
        }
        Role role = new Role(
                request.name()
        );

        roleDao.insertRole(role);
    }

    public void deleteRole(Integer id) {
        if (roleDao.existsRoleById(id)) {
            roleDao.deleteRoleById(id);
        } else {
            throw new ResourceNotFoundException("role with id [%s] not found".formatted(id));
        }
    }

    public void updateRole(Integer id, RoleUpdateRequest request) {
        Role role = getRole(id);
        boolean changes = false;

        if (request.name() != null && !request.name().equals(role.getName())) {
            role.setName(request.name());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        roleDao.updateRole(role);
    }
}
