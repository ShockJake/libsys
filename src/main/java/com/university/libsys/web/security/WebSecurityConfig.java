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

    private final String[] publicPages = new String[]{"/", "/about", "/createAccount", "/infoPage",
            "/style/**", "/svg/**", "/photo/**", "/login**", "/scripts/**"};
    private final String[] ignoreCSRF = new String[]{"/createAccount", "/user_management/**"};

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
                        .deleteCookies("JSESSIONID"))
                .cors().and().csrf().ignoringAntMatchers(ignoreCSRF);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
