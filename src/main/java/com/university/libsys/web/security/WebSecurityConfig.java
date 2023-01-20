package com.university.libsys.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final String[] publicPages = new String[]{"/", "/about", "/createAccount", "/infoPage", "/style/**",
            "/svg/**", "/photo/**", "/login**", "/scripts/**", "/books/**", "/libraries/**", "/selections/**",
            "/posts/photos/**", "/posts" , "/posts/user/**", "/request_management/create_request*"};

    private final String[] administrationPages = new String[]{"/user_management/**", "/request_management/**",
            "/post_management/**", "/administration/**"};

    private final String[] ignoreCSRF = new String[]{"/createAccount", "/user_management/**", "/posts/like/**",
            "/messages_management/**", "/request_management/**", "/post_management/**", "/posts/unlike/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeHttpRequests()
                .antMatchers(publicPages).permitAll()                   // Permitting public pages for all users
                .antMatchers(administrationPages).hasRole("ADMIN")      // Permitting administration pages for admin
                .anyRequest().authenticated()
                .and().formLogin((form) -> form.loginPage("/login")     // Configuring login process
                        .permitAll()
                        .defaultSuccessUrl("/account"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .cors().and().csrf().ignoringAntMatchers(ignoreCSRF);   // Ignoring CSRF on some pages

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
