package com.odyssey.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer>{
    boolean existsUserById(Integer id);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndRoleId(String username, Integer roleId);
    Optional<User> findByEmail(String email);
}
