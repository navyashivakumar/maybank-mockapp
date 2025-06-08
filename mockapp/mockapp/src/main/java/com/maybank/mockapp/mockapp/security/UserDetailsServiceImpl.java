package com.maybank.mockapp.mockapp.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!"maybank".equals(username)) {
            throw new UsernameNotFoundException("User not found");
        }

        return new User("maybank",
                new BCryptPasswordEncoder().encode("secure123"),
                Collections.emptyList());
    }
}

