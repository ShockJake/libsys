package com.university.libsys.web.security;

import com.university.libsys.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final String[] publicPages = new String[]{"/", "/about", "/style/**", "/svg/**",
            "/photo/**", "/login**", "/scripts/**"};
    private final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeHttpRequests()
                .antMatchers(publicPages).permitAll()
                .anyRequest().authenticated()
                .and().formLogin((form) -> form.loginPage("/login")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        final PasswordEncoder passwordEncoder = passwordEncoder();
        final List<UserDetails> userDetails = new ArrayList<>();
        userRepository.findAll().forEach(user -> userDetails.add(User
                .withUsername(user.getLogin())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getUserRole().name())
                .build()));
        log.debug("Initialized users");

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
