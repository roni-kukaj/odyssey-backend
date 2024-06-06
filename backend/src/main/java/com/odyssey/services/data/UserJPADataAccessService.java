package com.odyssey.services.data;

import com.odyssey.daos.UserDao;
import com.odyssey.models.User;
import com.odyssey.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userJPAService")
public class UserJPADataAccessService implements UserDao
{

    private final UserRepository userRepository;

    public UserJPADataAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> selectAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> selectUserById(Integer Id) {
        return userRepository.findById(Id);
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsUserByEmail(String email) {
       return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsUserById(Integer Id) {
       return userRepository.existsUserById(Id);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public void deleteUserById(Integer id) {userRepository.deleteById(id);}

    @Override
    public void updateUser(User user) {userRepository.save(user);}

}
