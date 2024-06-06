package com.odyssey.services;

import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.UserDao;
import com.odyssey.dtos.UserDto;
import com.odyssey.dtos.UserUpdateDto;
import com.odyssey.dtos.UserUpdateRequest;
import com.odyssey.services.file.FileService;
import com.odyssey.models.Role;
import com.odyssey.models.User;
import com.odyssey.daos.RoleDao;
import com.odyssey.services.utils.UserDtoMapper;
import com.odyssey.dtos.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.odyssey.exception.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;

    public UserService(
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("roleJPAService") RoleDao roleDao,
            CloudinaryService cloudinaryService,
            PasswordEncoder passwordEncoder,
            UserDtoMapper userDtoMapper
    ) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.cloudinaryService = cloudinaryService;
        this.passwordEncoder = passwordEncoder;
        this.userDtoMapper = userDtoMapper;
    }

    public List<UserDto> getAllUsers() {
        return userDao.selectAllUsers()
                .stream()
                .map(userDtoMapper)
                .collect(Collectors.toList());
    }

    public UserDto getUser(Integer id) {
        return userDao.selectUserById(id)
                .map(userDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(id)));
    }

    public UserDto getUserByUsername(String username) {
        return userDao.selectUserByUsername(username)
                .map(userDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("user with username [%s] not found".formatted(username)));
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

        Role role = roleDao.selectRoleById(1)
                .orElseThrow(() -> new ResourceNotFoundException("role with id [%s] not found".formatted(1)));

        User user = new User(
                userRegistrationRequest.fullname(),
                userRegistrationRequest.username(),
                userRegistrationRequest.email(),
                passwordEncoder.encode(userRegistrationRequest.password()),
                "https://res.cloudinary.com/dphboq54c/image/upload/v1715904645/Odyssey-DB/avatars/default-avatar_mfg3bf.png",
                role
        );

        userDao.insertUser(user);
    }

    public void addAdmin(UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.email();
        if (userDao.existsUserByEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }
        String username = userRegistrationRequest.username();
        if (userDao.existsUserByUsername(username)) {
            throw new DuplicateResourceException("username already taken");
        }

        Role role = roleDao.selectRoleById(2)
                .orElseThrow(() -> new ResourceNotFoundException("role with id [%s] not found".formatted(2)));

        User user = new User(
                userRegistrationRequest.fullname(),
                userRegistrationRequest.username(),
                userRegistrationRequest.email(),
                passwordEncoder.encode(userRegistrationRequest.password()),
                "https://res.cloudinary.com/dphboq54c/image/upload/v1715904645/Odyssey-DB/avatars/default-avatar_mfg3bf.png",
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

    public void updateUser(Integer id, UserUpdateDto dto) {
        User user = userDao.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(id)));

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
        try {
            if (dto.file() != null) {
                File file = FileService.convertFile(dto.file());
                String newUrl = cloudinaryService.uploadImage(file, "avatars");
                user.setAvatar(newUrl);
                changes = true;
            }
        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        userDao.updateUser(user);
    }
}
