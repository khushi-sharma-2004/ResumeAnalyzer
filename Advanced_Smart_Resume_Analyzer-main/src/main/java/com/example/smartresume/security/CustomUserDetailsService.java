package com.example.smartresume.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.smartresume.model.User;
import com.example.smartresume.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("➡️ Trying to log in with username: " + username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
              System.out.println("❌ User not found!");
              throw new UsernameNotFoundException("User not found: " + username);
            }

        System.out.println("✅ Found user in DB: " + user.getUsername());
        System.out.println("✅ Hashed password in DB: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

}
