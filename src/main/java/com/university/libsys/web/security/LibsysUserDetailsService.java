package com.university.libsys.web.security;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibsysUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public LibsysUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        final User user = Optional.ofNullable(userRepository.findUserByLogin(userLogin))
                .orElseThrow(() -> new UsernameNotFoundException(userLogin));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPassword())
                .roles(user.getUserRole().name())
                .build();
    }
}
