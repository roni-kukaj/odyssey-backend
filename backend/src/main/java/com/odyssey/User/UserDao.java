package com.odyssey.User;

import com.odyssey.User.User;

import java.util.List;

public interface UserDao{
    List<User> selectAllUsers();
}
