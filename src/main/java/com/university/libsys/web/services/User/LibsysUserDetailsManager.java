package com.university.libsys.web.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibsysUserDetailsManager implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

    @Autowired
    public LibsysUserDetailsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        final User user = Optional.ofNullable(userRepository.findUserByLogin(userLogin)).orElseThrow(() -> new UsernameNotFoundException(userLogin));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getUserRole().name())
                .build();
    }
}
