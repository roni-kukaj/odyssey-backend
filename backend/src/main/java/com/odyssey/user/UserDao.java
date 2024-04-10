package com.odyssey.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> selectAllUsers();
    Optional<User> selectUserById(Integer Id);
    void insertUser(User user);
    void updateUser(User user);
    boolean existsUserByEmail(String email);
    boolean existsUserById(Integer Id);
    boolean existsUserByUsername(String username);
    void deleteUserById(Integer Id);
}
