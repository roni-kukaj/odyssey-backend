package com.odyssey.services;

import com.odyssey.daos.UserDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public UsersDetailsService(@Qualifier("userJPAService") UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.selectUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
    }
}
