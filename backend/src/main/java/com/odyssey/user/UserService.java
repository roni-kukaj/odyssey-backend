package com.odyssey.user;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.fileService.FileService;
import com.odyssey.role.Role;
import com.odyssey.role.RoleDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.odyssey.exception.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final CloudinaryService cloudinaryService;

    public UserService(
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("roleJPAService") RoleDao roleDao,
            CloudinaryService cloudinaryService
    ) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.cloudinaryService = cloudinaryService;
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    public User getUser(Integer id) {
        return userDao.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(id)));
    }

    public void addUser(UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.email();
        if (userDao.existsUserByEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }
        String username = userRegistrationRequest.username();
        if (userDao.existsUserByUsername(username)) {
            throw new DuplicateResourceException("username already taken");
        }

        Role role = roleDao.selectRoleById(userRegistrationRequest.role_id())
                .orElseThrow(() -> new ResourceNotFoundException("role with id [%s] not found".formatted(userRegistrationRequest.role_id())));

        User user = new User(
                userRegistrationRequest.fullname(),
                userRegistrationRequest.username(),
                userRegistrationRequest.email(),
                userRegistrationRequest.password(),
                userRegistrationRequest.avatar(),
                role
        );

        userDao.insertUser(user);
    }

    public void deleteUser(Integer id) {
        if (userDao.existsUserById(id)) {
            userDao.deleteUserById(id);
        }
        else {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(id));
        }
    }

    public void updateUser(Integer id, UserUpdateInformationDto dto) {
        User user = getUser(id);
        boolean changes = false;

        if (dto.fullname() != null && !dto.fullname().equals(user.getFullname())) {
            user.setFullname(dto.fullname());
            changes = true;
        }
        if (dto.username() != null && !dto.username().equals(user.getUsername())) {
            if (userDao.existsUserByUsername(dto.username())) {
                throw new DuplicateResourceException("username already taken");
            }
            user.setUsername(dto.username());
            changes = true;
        }
        if (dto.password() != null && !dto.password().equals(user.getPassword())) {
            user.setPassword(dto.password());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }
        userDao.updateUser(user);
    }

    public void updateUserAvatar(Integer id, MultipartFile image) {
        User user = getUser(id);
        try {
            File file = FileService.convertFile(image);
            String newUrl = cloudinaryService.uploadImage(file, "avatars");
            user.setAvatar(newUrl);
            userDao.updateUser(user);
        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
    }

    public void updateUser(Integer id, UserUpdateRequest updateRequest) {
        User user = getUser(id);
        boolean changes = false;

        if (updateRequest.fullname() != null && !updateRequest.fullname().equals(user.getFullname())) {
            user.setFullname(updateRequest.fullname());
            changes = true;
        }
        if (updateRequest.username() != null && !updateRequest.username().equals(user.getUsername())) {
            if (userDao.existsUserByUsername(updateRequest.username())) {
                throw new DuplicateResourceException("username already taken");
            }
            user.setUsername(updateRequest.username());
            changes = true;
        }
        if (updateRequest.email() != null && !updateRequest.email().equals(user.getEmail())) {
            if (userDao.existsUserByEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            user.setEmail(updateRequest.email());
            changes = true;
        }
        if (updateRequest.password() != null && !updateRequest.password().equals(user.getPassword())) {
            user.setPassword(updateRequest.password());
            changes = true;
        }
        if (updateRequest.avatar() != null && !updateRequest.avatar().equals(user.getAvatar())) {
            user.setAvatar(updateRequest.avatar());
            changes = true;
        }
        if (updateRequest.role_id() != null && !updateRequest.role_id().equals(user.getRole().getId())) {
            Role role = roleDao.selectRoleById(updateRequest.role_id())
                    .orElseThrow(() -> new ResourceNotFoundException("role with id [%s] not found".formatted(updateRequest.role_id())));

            user.setRole(role);
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }
        userDao.updateUser(user);
    }

}
